package com.dayton.drone.ble.model.request.notification;

import android.content.Context;
import android.util.Log;

import com.dayton.drone.ble.model.ancs.NotificationDataSource;
import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.server.GattServerService;

/**
 * Created by med on 16/4/25.
 */
public class DroneNotificationTrigger {
    final private NotificationDataSource dataSource;
    final GattServerService gattServerService;
    public DroneNotificationTrigger(GattServerService gattServerService,NotificationDataSource dataSource){
        this.gattServerService = gattServerService;
        this.dataSource = dataSource;
    }

    public void doNotificationAlert(){
        if(gattServerService==null)
        {
            Log.e("NotificationTrigger","gattServerService is null, disable trigger alert notification");
            return;
        }
    }
}
