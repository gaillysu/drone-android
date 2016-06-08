package com.dayton.drone.ble.model.request.notification;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/25.
 */
public class DroneNotificationRequest extends RequestBase {
    protected final static byte HEADER = (byte)0xF0;
    public DroneNotificationRequest(Context context) {
        super(context);
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
