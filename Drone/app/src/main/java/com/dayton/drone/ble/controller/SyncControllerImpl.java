package com.dayton.drone.ble.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.ble.datasource.GattAttributesDataSourceImpl;
import com.dayton.drone.ble.model.packet.DronePacket;
import com.dayton.drone.ble.model.request.init.SetRTCRequest;

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

/**
 * Created by med on 16/4/12.
 */
public class SyncControllerImpl implements  SyncController{

    final static String TAG = SyncControllerImpl.class.getName();
    final ApplicationModel application;
    private ConnectionController connectionController;
    private List<MEDRawData> packetsBuffer = new ArrayList<MEDRawData>();

    public  SyncControllerImpl(ApplicationModel application){
        this.application = application;
        connectionController = ConnectionController.Singleton.getInstance(application,new GattAttributesDataSourceImpl(application));
        EventBus.getDefault().register(this);
    }

    @Override
    public void startConnect(boolean forceScan) {
        if(forceScan){
            connectionController.forgetSavedAddress();
        }
        connectionController.connect();
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

            packetsBuffer.add(droneData);
            if((byte)0x80 == droneData.getRawData()[0])
            {
                QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).next();

                DronePacket packet = new DronePacket(packetsBuffer);
                //if packets invaild, discard them, and reset buffer
                if(!packet.isVaildPackets())
                {
                    Log.e("Nevo Error","InVaild Packets Received!");
                    packetsBuffer.clear();
                    return;
                }
                if((byte) SetRTCRequest.HEADER == droneData.getRawData()[1])
                {
                    //set Profile

                }
                packetsBuffer.clear();
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
                    sendRequest(new SetRTCRequest((Context) application));
                }
            }, 2000);
        } else {
            QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).clear();
            packetsBuffer.clear();
        }
    }
}
