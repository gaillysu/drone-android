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
import com.dayton.drone.ble.model.TimeZoneModel;
import com.dayton.drone.ble.model.packet.ActivityPacket;
import com.dayton.drone.ble.model.packet.GetBatteryPacket;
import com.dayton.drone.ble.model.packet.GetStepsGoalPacket;
import com.dayton.drone.ble.model.packet.SystemEventPacket;
import com.dayton.drone.ble.model.packet.SystemStatusPacket;
import com.dayton.drone.ble.model.packet.base.DronePacket;
import com.dayton.drone.ble.model.request.battery.GetBatteryRequest;
import com.dayton.drone.ble.model.request.clean.ForgetWatchRequest;
import com.dayton.drone.ble.model.request.init.GetSystemStatus;
import com.dayton.drone.ble.model.request.init.SetAppConfigRequest;
import com.dayton.drone.ble.model.request.init.SetRTCRequest;
import com.dayton.drone.ble.model.request.init.SetSystemConfig;
import com.dayton.drone.ble.model.request.setting.SetGoalRequest;
import com.dayton.drone.ble.model.request.setting.SetStepsToWatchReuqest;
import com.dayton.drone.ble.model.request.setting.SetUserProfileRequest;
import com.dayton.drone.ble.model.request.sync.GetActivityRequest;
import com.dayton.drone.ble.model.request.sync.GetStepsGoalRequest;
import com.dayton.drone.ble.model.request.worldclock.SetWorldClockRequest;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.event.BLEPairStatusChangedEvent;
import com.dayton.drone.event.BatteryStatusChangedEvent;
import com.dayton.drone.event.BigSyncEvent;
import com.dayton.drone.event.DownloadStepsEvent;
import com.dayton.drone.event.GoalCompletedEvent;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.event.LowMemoryEvent;
import com.dayton.drone.event.ProfileChangedEvent;
import com.dayton.drone.event.StepsGoalChangedEvent;
import com.dayton.drone.event.Timer10sEvent;
import com.dayton.drone.event.WorldClockChangedEvent;
import com.dayton.drone.model.DailySteps;
import com.dayton.drone.model.Steps;
import com.dayton.drone.model.WorldClock;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.Common;
import com.dayton.drone.utils.SpUtils;
import com.dayton.drone.utils.StepsHandler;

import net.medcorp.library.android.notificationsdk.listener.ListenerService;
import net.medcorp.library.ble.controller.ConnectionController;
import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.event.BLEResponseDataEvent;
import net.medcorp.library.ble.model.request.BLERequestData;
import net.medcorp.library.ble.model.response.BLEResponseData;
import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.Optional;
import net.medcorp.library.ble.util.QueuedMainThreadHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by med on 16/4/12.
 */
public class SyncControllerImpl implements  SyncController{

    final static String TAG = SyncController.class.getSimpleName();
    final ApplicationModel application;
    private ConnectionController connectionController;
    private List<MEDRawData> packetsBuffer = new ArrayList<MEDRawData>();

    private Timer autoSyncTimer = null;
    private Optional<Date> theBigSyncStartDate = new Optional<>();
    private int baseSteps = 0;
    private final long BIG_SYNC_INTERVAL = 5*60*1000l; //5minutes
    private final long LITTLE_SYNC_INTERVAL = 10000l;   //10s

    private void startAutoSyncTimer() {
        if(autoSyncTimer!=null)autoSyncTimer.cancel();
        autoSyncTimer = new Timer();
        autoSyncTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //send little sync request
                sendRequest(new GetStepsGoalRequest(application));
                //send big sync request
                if((new Date().getTime() - SpUtils.getLongMethod(application,CacheConstants.LAST_BIG_SYNC_TIMESTAMP,new Date().getTime()))>BIG_SYNC_INTERVAL)
                {
                    sendRequest(new GetActivityRequest(application));
                }
                EventBus.getDefault().post(new Timer10sEvent());
                startAutoSyncTimer();
            }
        },LITTLE_SYNC_INTERVAL);
    }

    public  SyncControllerImpl(ApplicationModel application){
        this.application = application;
        connectionController = ConnectionController.Singleton.getInstance(application,new GattAttributesDataSourceImpl(application));
        EventBus.getDefault().register(this);
        ServiceConnection serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.v(TAG, name + " Service disconnected");
                localBinder = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.v(TAG, name + " Service connected");
                localBinder = (LocalService.LocalBinder) service;
            }
        };
        application.getApplicationContext().bindService(new Intent(application, LocalService.class), serviceConnection, Activity.BIND_AUTO_CREATE);
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
    public void disConnect() {
        connectionController.disconnect();
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
        //steps0:clean pair infomation on watch and drop connection
        sendRequest(new ForgetWatchRequest(application));
        //step1:disconnect
        if(connectionController.isConnected())
        {
            connectionController.disconnect();
        }
        //step2:unpair this watch from system bluetooth setting
        connectionController.unPairDevice();
        //step3:reset MAC address and firstly run flag and big sync stamp
        connectionController.forgetSavedAddress();

    }

    @Override
    public void findDevice() {

    }

    @Override
    public void getBattery() {
        sendRequest(new GetBatteryRequest(application));
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

    private void setWorldClock(List<WorldClock> worldClockList)
    {
        List<TimeZoneModel> timeZoneModelList = new ArrayList<>();
        for(WorldClock worldClock:worldClockList)
        {
            TimeZone timeZone = TimeZone.getTimeZone(worldClock.getTimeZoneName());
            Calendar LATime = new GregorianCalendar(timeZone);
            LATime.setTimeInMillis(new Date().getTime());
            float utc_offset = timeZone.getOffset(LATime.getTimeInMillis())/1000f/3600f;
            String utc_name = worldClock.getTimeZoneTitle().split(",")[0];
            Log.i("gailly",utc_name + ", " + utc_offset+ ", DaylightTime: " + timeZone.useDaylightTime());
            timeZoneModelList.add(new TimeZoneModel(utc_offset,utc_name));
        }
        sendRequest(new SetWorldClockRequest(application,timeZoneModelList));
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
                Log.e("Drone Error","dummy Packets Received!");
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
                    Log.e(TAG,"InVaild Packets Received!");
                    packetsBuffer.clear();
                    QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).next();
                    return;
                }
                if(GetSystemStatus.HEADER == packet.getHeader())
                {
                    SystemStatusPacket systemStatusPacket = packet.newSystemStatusPacket();
                    Log.i(TAG,"GetSystemStatus return status value: " + systemStatusPacket.getStatus());
                    Date date = new Date(SpUtils.getLongMethod(application, CacheConstants.TODAY_DATE, 0));
                    if(SpUtils.getBoolean(application,CacheConstants.TODAY_RESET,false) && Common.removeTimeFromDate(date).getTime() == Common.removeTimeFromDate(new Date()).getTime())
                    {
                        baseSteps = SpUtils.getIntMethod(application, CacheConstants.TODAY_BASESTEP, 0);
                    }
                    else {
                        baseSteps = 0;
                    }
                    if((systemStatusPacket.getStatus() & Constants.SystemStatus.SystemReset.rawValue())== Constants.SystemStatus.SystemReset.rawValue())
                    {
                        SpUtils.putBoolean(application,CacheConstants.TODAY_RESET,true);
                        sendRequest(new SetSystemConfig(application,1,0, 0, 0, Constants.SystemConfigID.ClockFormat));
                        sendRequest(new SetSystemConfig(application,1,0, 0, 0, Constants.SystemConfigID.Enabled));
                        sendRequest(new SetSystemConfig(application,1,0, 0, 0, Constants.SystemConfigID.SleepConfig));
                        sendRequest(new SetRTCRequest(application));
                        sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.WorldClock));
                        sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.ActivityTracking));
                        sendRequest(new SetUserProfileRequest(application,application.getUser()));
                        //set goal to watch
                        sendRequest(new SetGoalRequest(application, SpUtils.getIntMethod(application, CacheConstants.GOAL_STEP, 10000)));
                        //if the cached date is today,use the cached steps to set watch
                        if (Common.removeTimeFromDate(date).getTime() == Common.removeTimeFromDate(new Date()).getTime())
                        {
                            //set steps to watch
                            baseSteps = SpUtils.getIntMethod(application, CacheConstants.TODAY_STEP, 0);
                            SpUtils.putIntMethod(application, CacheConstants.TODAY_BASESTEP, baseSteps);
                            sendRequest(new SetStepsToWatchReuqest(application,baseSteps));
                        }
                        //set world colock to watch
                        setWorldClock(application.getWorldClockDatabaseHelper().getSelected());
                    }
                    else if((systemStatusPacket.getStatus() & Constants.SystemStatus.InvalidTime.rawValue())==Constants.SystemStatus.InvalidTime.rawValue())
                    {
                        sendRequest(new SetRTCRequest(application));
                        sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.WorldClock));
                        sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.ActivityTracking));
                        sendRequest(new SetUserProfileRequest(application,application.getUser()));
                    }
                    if((systemStatusPacket.getStatus() & Constants.SystemStatus.GoalCompleted.rawValue())==Constants.SystemStatus.GoalCompleted.rawValue())
                    {
                        EventBus.getDefault().post(new GoalCompletedEvent());
                        sendRequest(new SetGoalRequest(application,SetGoalRequest.DEFAULTSTEPSGOAL));
                    }
                    if((systemStatusPacket.getStatus() & Constants.SystemStatus.ActivityDataAvailable.rawValue())==Constants.SystemStatus.ActivityDataAvailable.rawValue())
                    {
                        theBigSyncStartDate = new Optional<>(new Date());
                        EventBus.getDefault().post(new BigSyncEvent(theBigSyncStartDate.get(), BigSyncEvent.BIG_SYNC_EVENT.STARTED));
                        sendRequest(new GetActivityRequest(application));
                    }
                    if((systemStatusPacket.getStatus() & Constants.SystemStatus.SubscribedToNotifications.rawValue())==Constants.SystemStatus.SubscribedToNotifications.rawValue())
                    {
                        Log.d(TAG,"Subscribed to notifications success.");
                    }
                    //here start ANCS service
                    application.getApplicationContext().bindService(new Intent(application, ListenerService.class), notificationServiceConnection, Activity.BIND_AUTO_CREATE);
                    //here start little sync timer
                    startAutoSyncTimer();
                }
                else if(GetActivityRequest.HEADER == packet.getHeader())
                {
                    ActivityPacket activityPacket = packet.newActivityPacket();
                    Log.i(TAG,activityPacket.getDate().toString() + " time frame steps: " + activityPacket.getSteps());
                    Steps steps = new Steps(activityPacket.getSteps(), activityPacket.getDate().getTime());
                    steps.setDate((Common.removeTimeFromDate(new Date(activityPacket.getDate().getTime()))).getTime());
                    steps.setUserID(application.getUser().getUserID());
                    steps.setStepsGoal(SpUtils.getIntMethod(application, CacheConstants.GOAL_STEP, 10000));
                    application.getStepsDatabaseHelper().update(steps);
                    //save the oldest activity date as colud sync starting date
                    if(theBigSyncStartDate.isEmpty() || (theBigSyncStartDate.notEmpty() && theBigSyncStartDate.get().getTime()>steps.getDate()))
                    {
                        theBigSyncStartDate.set(new Date(steps.getDate()));
                    }
                    if(activityPacket.getMore()==Constants.ActivityDataStatus.MoreData.rawValue())
                    {
                        sendRequest(new GetActivityRequest(application));
                    }
                    else
                    {
                        EventBus.getDefault().post(new BigSyncEvent(theBigSyncStartDate.get(), BigSyncEvent.BIG_SYNC_EVENT.STOPPED));
                        SpUtils.putLongMethod(application, CacheConstants.LAST_BIG_SYNC_TIMESTAMP, new Date().getTime());
                    }
                }
                else if(GetStepsGoalRequest.HEADER == packet.getHeader())
                {
                    GetStepsGoalPacket getStepsGoalPacket = packet.newGetStepsGoalPacket();
                    int steps = getStepsGoalPacket.getSteps();
                    int goal  = getStepsGoalPacket.getGoal();
                    Log.i(TAG,"steps: " + steps + ",goal: " + goal + ",baseSteps: " + baseSteps);
                    //IMPORTANT NOTICE: above "steps" is a accumulated value that comes from the last reset, it is not a total steps for a day.
                    SpUtils.putIntMethod(application, CacheConstants.GOAL_STEP, goal);
                    SpUtils.putIntMethod(application, CacheConstants.TODAY_STEP,baseSteps + steps);
                    SpUtils.putLongMethod(application, CacheConstants.TODAY_DATE, new Date().getTime());
                    EventBus.getDefault().post(new LittleSyncEvent(steps, goal));
                }
                else if(GetBatteryRequest.HEADER == packet.getHeader())
                {
                    GetBatteryPacket getBatteryPacket = packet.newGetBatteryPacket();
                    EventBus.getDefault().post(new BatteryStatusChangedEvent(getBatteryPacket.getBatteryStatus(), getBatteryPacket.getBatteryLevel()));
                }
                //system event: 0x02, this is sent by watch proactively,pls refer to Constants.SystemEvent
                else if(SystemEventPacket.HEADER == packet.getHeader())
                {
                    SystemEventPacket systemEventPacket = packet.newSystemEventPacket();
                    Log.i(TAG,"SystemEventPacket return event value: " + systemEventPacket.getEvent());

                    if(systemEventPacket.getEvent() == Constants.SystemEvent.BatteryStatusChanged.rawValue()) {
                        getBattery();
                    }
                    else if(systemEventPacket.getEvent() == Constants.SystemEvent.GoalCompleted.rawValue()) {
                        sendRequest(new GetActivityRequest(application));
                        EventBus.getDefault().post(new GoalCompletedEvent());
                    }
                    else if(systemEventPacket.getEvent() == Constants.SystemEvent.LowMemory.rawValue()) {
                        EventBus.getDefault().post(new LowMemoryEvent());
                    }
                    else if(systemEventPacket.getEvent() == Constants.SystemEvent.ActivityDataAvailable.rawValue()) {
                        theBigSyncStartDate = new Optional<>(new Date());
                        EventBus.getDefault().post(new BigSyncEvent(theBigSyncStartDate.get(), BigSyncEvent.BIG_SYNC_EVENT.STARTED));
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
                    //reset last big sync timestamp every got connected.
                    SpUtils.putLongMethod(application, CacheConstants.LAST_BIG_SYNC_TIMESTAMP, new Date().getTime());
                    sendRequest(new GetSystemStatus(application));
                }
            }, 2000);
        } else {
            QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).clear();
            packetsBuffer.clear();
        }
    }

    @Subscribe
    public void onEvent(BLEPairStatusChangedEvent pairStateChangedEvent) {
        if(pairStateChangedEvent.getStatus() == BluetoothDevice.BOND_BONDED
                || pairStateChangedEvent.getStatus() == BluetoothDevice.BOND_NONE) {
            //THIS BELOW CODE SPEND MY 2 HOURS,DIRECTLY INVOKING WILL LEAD TO "NO FOUND SERVICES" ISSUE.
            //onEvent() is invoked by other thread
            //here when got paired, need restart connect, we should excute these code in the main thread
            //here defer 2s to invoke disConnect(),for some reason,the lower BT layer doesn't get realy disconnect
            //if right now disconnect it in the app layer,app.ConnectionController will not receive connection change (disconnection status) before invoking startConnect()
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    disConnect();
                    //IMPORTANT here has a risk: startLeScan() can't find the watch in some phone models, such as nexus 5
                    startConnect(false);
                }
            },2000);
        }
    }

    @Subscribe
    public void onEvent(WorldClockChangedEvent worldClockChangedEvent) {
        setWorldClock(worldClockChangedEvent.getWorldClockList());
    }
    @Subscribe
    public void onEvent(StepsGoalChangedEvent stepsGoalChangedEvent) {
        sendRequest(new SetGoalRequest(application,stepsGoalChangedEvent.getStepsGoal()));
    }
    @Subscribe
    public void onEvent(ProfileChangedEvent profileChangedEvent) {
        sendRequest(new SetUserProfileRequest(application,profileChangedEvent.getUser()));
    }

    @Subscribe
    public void onEvent(final DownloadStepsEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (event.getStatus() == DownloadStepsEvent.DOWNLOAD_STEPS_EVENT.STOPPED) {
                    StepsHandler stepsHandler = new StepsHandler(application.getStepsDatabaseHelper(), application.getUser());
                    DailySteps dailySteps= stepsHandler.getDailySteps(new Date());
                    if(dailySteps.getDailySteps()>SpUtils.getIntMethod(application, CacheConstants.TODAY_STEP, 0))
                    {
                        baseSteps = dailySteps.getDailySteps();
                        SpUtils.putLongMethod(application, CacheConstants.TODAY_DATE, new Date().getTime());
                        SpUtils.putIntMethod(application, CacheConstants.TODAY_BASESTEP, baseSteps);
                        SpUtils.putIntMethod(application,CacheConstants.TODAY_STEP,baseSteps);
                        sendRequest(new SetStepsToWatchReuqest(application,baseSteps));
                    }
                }
            }
        });
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
                    Log.i("LocalService","Ble pair state got changed:" + connectState + ",device:" + device.getAddress());
                    if(BluetoothDevice.BOND_BONDED == connectState || BluetoothDevice.BOND_NONE == connectState ) {
                        EventBus.getDefault().post(new BLEPairStatusChangedEvent(connectState));
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
            //you can add some functions here
        }

    }
    private LocalService.LocalBinder localBinder = null;

    private ServiceConnection notificationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(TAG, name+" Service disconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, name+" Service connected");
        }
    };
}
