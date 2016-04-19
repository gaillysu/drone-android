package com.dayton.drone.ble.controller;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.ble.datasource.GattAttributesDataSourceImpl;
import com.dayton.drone.ble.model.packet.ActivityPacket;
import com.dayton.drone.ble.model.packet.GetStepsGoalPacket;
import com.dayton.drone.ble.model.packet.SystemEventPacket;
import com.dayton.drone.ble.model.packet.SystemStatusPacket;
import com.dayton.drone.ble.model.packet.base.DronePacket;
import com.dayton.drone.ble.model.request.battery.GetBatteryRequest;
import com.dayton.drone.ble.model.request.init.GetSystemStatus;
import com.dayton.drone.ble.model.request.init.SetAppConfigRequest;
import com.dayton.drone.ble.model.request.init.SetRTCRequest;
import com.dayton.drone.ble.model.request.init.SetSystemConfig;
import com.dayton.drone.ble.model.request.setting.SetUserProfileRequest;
import com.dayton.drone.ble.model.request.sync.GetActivityRequest;
import com.dayton.drone.ble.model.request.sync.GetStepsGoalRequest;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.event.BatteryStatusChangedEvent;
import com.dayton.drone.event.BigSyncEvent;
import com.dayton.drone.event.GoalCompletedEvent;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.event.LowMemoryEvent;
import com.dayton.drone.event.TimerEvent;

import net.medcorp.library.ble.controller.ConnectionController;
import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.event.BLEResponseDataEvent;
import net.medcorp.library.ble.model.request.BLERequestData;
import net.medcorp.library.ble.model.response.BLEResponseData;
import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.QueuedMainThreadHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by med on 16/4/12.
 */
public class SyncControllerImpl implements  SyncController{

    final static String TAG = SyncControllerImpl.class.getName();
    final ApplicationModel application;
    private ConnectionController connectionController;
    private List<MEDRawData> packetsBuffer = new ArrayList<MEDRawData>();

    private Timer autoSyncTimer = null;

    private void startAutoSyncTimer() {
        if(autoSyncTimer!=null)autoSyncTimer.cancel();
        autoSyncTimer = new Timer();
        autoSyncTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //TODO : connected with watch is idle status, enable this little sync request.
                //sendRequest(new GetStepsGoalRequest(application));
                EventBus.getDefault().post(new TimerEvent());
                startAutoSyncTimer();
            }
        },10000);
    }

    public  SyncControllerImpl(ApplicationModel application){
        this.application = application;
        connectionController = ConnectionController.Singleton.getInstance(application,new GattAttributesDataSourceImpl(application));
        EventBus.getDefault().register(this);
        application.getApplicationContext().bindService(new Intent(application,LocalService.class), serviceConnection, Activity.BIND_AUTO_CREATE);
        startAutoSyncTimer();
    }

    @Override
    public void startConnect(boolean forceScan) {
        if(forceScan){
            connectionController.forgetSavedAddress();
        }
        connectionController.scan();
    }

    @Override
    public boolean isConnected() {
        return connectionController.isConnected();
    }

    @Override
    public String getFirmwareVersion() {
        return connectionController.getBluetoothVersion();
    }

    @Override
    public String getSoftwareVersion() {
        return connectionController.getSoftwareVersion();
    }

    @Override
    public void forgetDevice() {
        //step1:disconnect
        if(connectionController.isConnected())
        {
            connectionController.disconnect();
        }
        //step2:unpair this watch from system bluetooth setting
        connectionController.unPairDevice();
        //step3:reset MAC address and firstly run flag and big sync stamp
        connectionController.forgetSavedAddress();

        //TODO reset big sync and first run flag
    }

    @Override
    public void findDevice() {

    }


    /**
     * send request  package to watch by using a queue
     * @param request
     */

    private void sendRequest(final BLERequestData request) {
        if(connectionController.inOTAMode()) {
            return;
        }
        if(!isConnected()) {
            return;
        }
        QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, request.getClass().getName());
                connectionController.sendRequest(request);
            }
        });
    }

    @Subscribe
    public void onEvent(BLEResponseDataEvent eventData)
    {
        BLEResponseData data = eventData.getData();
        if(data.getType().equals(MEDRawData.TYPE))
        {
            final MEDRawData droneData = (MEDRawData) data;

            if(droneData.getRawData().length==1 && (byte)0xFF == droneData.getRawData()[0])
            {
                //discard dummy packet "FF"
                return;
            }
            packetsBuffer.add(droneData);
            //packet format: 0x80 HEADER .......  , no fixed length
            if((byte)0x80 == droneData.getRawData()[0])
            {
                DronePacket packet = new DronePacket(packetsBuffer);
                //if packets invaild, discard them, and reset buffer
                if(!packet.isVaildPackets())
                {
                    Log.e("Nevo Error","InVaild Packets Received!");
                    packetsBuffer.clear();
                    QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).next();
                    return;
                }
                if((byte) GetSystemStatus.HEADER == packet.getHeader())
                {
                    SystemStatusPacket systemStatusPacket = packet.newSystemStatusPacket();
                    Log.i(TAG,"GetSystemStatus return status value: " + systemStatusPacket.getStatus());
                    if(systemStatusPacket.getStatus()== Constants.SystemStatus.SystemReset.rawValue()
                            || systemStatusPacket.getStatus()==Constants.SystemStatus.InvalidTime.rawValue())
                    {
                        sendRequest(new SetSystemConfig(application));
                        sendRequest(new SetRTCRequest(application));
                        sendRequest(new SetAppConfigRequest(application));
                        sendRequest(new SetUserProfileRequest(application));
                    }

                    if(systemStatusPacket.getStatus()==Constants.SystemStatus.InvalidTime.rawValue())
                    {
                        sendRequest(new SetRTCRequest(application));
                    }
                    else if(systemStatusPacket.getStatus()==Constants.SystemStatus.GoalCompleted.rawValue())
                    {
                        EventBus.getDefault().post(new GoalCompletedEvent());
                        sendRequest(new GetActivityRequest(application));
                    }
                    else if(systemStatusPacket.getStatus()==Constants.SystemStatus.ActivityDataAvailable.rawValue())
                    {
                        EventBus.getDefault().post(new BigSyncEvent(BigSyncEvent.BIG_SYNC_EVENT.STARTED));
                        sendRequest(new GetActivityRequest(application));
                    }
                }
                else if((byte) SetRTCRequest.HEADER == packet.getHeader())
                {
                    sendRequest(new SetAppConfigRequest(application));
                }
                else if((byte) SetAppConfigRequest.HEADER == packet.getHeader())
                {
                    sendRequest(new SetUserProfileRequest(application));
                }
                else if((byte) GetActivityRequest.HEADER == packet.getHeader())
                {
                    ActivityPacket activityPacket = packet.newActivityPacket();
                    Log.i(TAG,activityPacket.getDate().toString() + " steps: " + activityPacket.getSteps());
                    //TODO, save it to "steps" table

                    if(activityPacket.getMore()==Constants.ActivityDataStatus.MoreData.rawValue())
                    {
                        sendRequest(new GetActivityRequest(application));
                    }
                    else
                    {
                        EventBus.getDefault().post(new BigSyncEvent(BigSyncEvent.BIG_SYNC_EVENT.STOPPED));
                    }
                }
                else if((byte) GetStepsGoalRequest.HEADER == packet.getHeader())
                {
                    GetStepsGoalPacket getStepsGoalPacket = packet.newGetStepsGoalPacket();
                    int steps = getStepsGoalPacket.getSteps();
                    int goal  = getStepsGoalPacket.getGoal();
                    Log.i(TAG,"steps: " + steps + ",goal: " + goal);
                    //TODO, update "steps" table, and refresh screen

                    EventBus.getDefault().post(new LittleSyncEvent(steps, goal));
                }
                else if((byte) SystemEventPacket.HEADER == packet.getHeader())
                {
                    SystemEventPacket systemEventPacket = packet.newSystemEventPacket();
                    Log.i(TAG,"SystemEventPacket return event value: " + systemEventPacket.getEvent());

                    if(systemEventPacket.getEvent() == Constants.SystemEvent.BatteryStatusChanged.rawValue()) {
                        sendRequest(new GetBatteryRequest(application));
                        EventBus.getDefault().post(new BatteryStatusChangedEvent());
                    }
                    else if(systemEventPacket.getEvent() == Constants.SystemEvent.GoalCompleted.rawValue()) {
                        sendRequest(new GetActivityRequest(application));
                        EventBus.getDefault().post(new GoalCompletedEvent());
                    }
                    else if(systemEventPacket.getEvent() == Constants.SystemEvent.LowMemory.rawValue()) {
                        EventBus.getDefault().post(new LowMemoryEvent());
                    }
                    else if(systemEventPacket.getEvent() == Constants.SystemEvent.ActivityDataAvailable.rawValue()) {
                        EventBus.getDefault().post(new BigSyncEvent(BigSyncEvent.BIG_SYNC_EVENT.STARTED));
                        sendRequest(new GetActivityRequest(application));
                    }
                }
                //process done current cmd's response, request next cmd
                packetsBuffer.clear();
                QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).next();
            }
        }
    }

    @Subscribe
    public void onEvent(BLEConnectionStateChangedEvent stateChangedEvent) {
        if(stateChangedEvent.isConnected()) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    packetsBuffer.clear();
                    sendRequest(new GetSystemStatus(application));
                }
            }, 2000);
        } else {
            QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).clear();
            packetsBuffer.clear();
        }
    }

    //local service
    static public class LocalService extends Service
    {
        BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
                {
                    Log.i("LocalService","Screen On");
                    if(!ConnectionController.Singleton.getInstance(context, new GattAttributesDataSourceImpl(context)).isConnected())
                    {
                        ConnectionController.Singleton.getInstance(context,new GattAttributesDataSourceImpl(context)).scan();
                    }
                }
                if(intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
                {
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    int connectState = device.getBondState();
                    //TODO , set up watch here when paired success, should macth address with the saved.
                    if(BluetoothDevice.BOND_BONDED == connectState ) {

                    }
                }

            }
        };

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return new LocalBinder();
        }

        @Override
        public void onCreate() {
            super.onCreate();
            registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
            registerReceiver(myReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            unregisterReceiver(myReceiver);
        }

        public class LocalBinder extends Binder {
            //todo .you can add some functions here
        }

    }
    private LocalService.LocalBinder localBinder = null;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(TAG, name+" Service disconnected");
            localBinder = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, name+" Service connected");
            localBinder = (LocalService.LocalBinder)service;
        }
    };
}
