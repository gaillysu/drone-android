package com.dayton.drone.ble.model.request.sync;


import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.ble.datasource.GattAttributesDataSourceImpl;
import com.dayton.drone.ble.model.packet.base.DronePacket;
import com.dayton.drone.event.BLEPairStatusChangedEvent;


import net.medcorp.library.ble.controller.ConnectionController;
import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.event.BLEResponseDataEvent;
import net.medcorp.library.ble.model.response.BLEResponseData;
import net.medcorp.library.ble.model.response.MEDRawData;

import org.apache.commons.codec.binary.Hex;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by med on 16/4/26.
 */
public class GetActivityRequestTest extends AndroidTestCase {
    private final String TAG = GetActivityRequestTest.class.getName();
    private ConnectionController connectionController;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        connectionController = ConnectionController.Singleton.getInstance(getContext(),new GattAttributesDataSourceImpl(getContext()));
        EventBus.getDefault().register(this);
    }

    public void testConnect(){
        connectionController.scan();
    }
    @Subscribe
    public void onEvent(BLEConnectionStateChangedEvent stateChangedEvent) {
        if(stateChangedEvent.isConnected()) {
            connectionController.sendRequest(new GetActivityRequest(getContext()));
        }
    }

    @Subscribe
    public void onEvent(BLEPairStatusChangedEvent pairStateChangedEvent) {
        if(pairStateChangedEvent.getStatus() == BluetoothDevice.BOND_BONDED
                || pairStateChangedEvent.getStatus() == BluetoothDevice.BOND_NONE) {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    connectionController.disconnect();
                    connectionController.scan();
                }
            }, 2000);
        }
    }

    @Subscribe
    public void onEvent(BLEResponseDataEvent eventData) {
        BLEResponseData data = eventData.getData();
        if(data.getType().equals(MEDRawData.TYPE)) {
            final MEDRawData droneData = (MEDRawData) data;
            if (droneData.getRawData().length == 1 && (byte) 0xFF == droneData.getRawData()[0]) {
                //discard dummy packet "FF"
                Log.e("Error", "dummy Packets Received!");
                return;
            }
            List<MEDRawData> listData = new ArrayList<>();
            listData.add(droneData);
            DronePacket packet = new DronePacket(listData);
            if(packet.getHeader() == GetActivityRequest.HEADER)
            {
                Log.i(TAG, "Receive Data " + new String(Hex.encodeHex(droneData.getRawData())));
            }
        }

    }

}
