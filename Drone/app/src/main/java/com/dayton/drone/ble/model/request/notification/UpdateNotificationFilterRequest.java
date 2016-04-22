package com.dayton.drone.ble.model.request.notification;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/22.
 */
public class UpdateNotificationFilterRequest extends RequestBase {
    public final static byte HEADER = (byte)0xB;
    final byte operationMode;
    final String appPackage;
    public UpdateNotificationFilterRequest(Context context, byte operationMode, String appPackage) {
        super(context);
        this.operationMode = operationMode;
        this.appPackage = appPackage;
    }

    @Override
    public byte[] getRawData() {
        byte[] rawData = new byte[4+appPackage.length()];
        rawData[0] = (byte)0x80;
        rawData[1] = HEADER;
        rawData[2] = operationMode;
        rawData[3] = (byte)appPackage.length();
        System.arraycopy(appPackage.getBytes(),0,rawData,4,appPackage.length());
        return rawData;
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
