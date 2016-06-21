package com.dayton.drone.ble.server;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import net.medcorp.library.android.notificationsdk.gatt.GattServer;

/**
 * Created by med on 16/6/6.
 */
public class GattServerService extends Service {
    public class LocalBinder extends Binder {

      public GattServerService getService() {
            return GattServerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean initialize() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            && ((BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter().isMultipleAdvertisementSupported())
        {
            GattServer.initialize(this);
            Log.w("Karl","Initialize here!");
            return true;
        }
        return false;
    }
}
