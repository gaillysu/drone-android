package com.dayton.drone.ble.model.request.notificationfilter;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/22.
 */
public class SetNotificationFilterRequest extends RequestBase {
    public final static byte HEADER = (byte)0xA;
    final byte operationMode;
    final byte forceClearList;

    public SetNotificationFilterRequest(Context context, byte operationMode, byte forceClearList) {
        super(context);
        this.operationMode = operationMode;
        this.forceClearList = forceClearList;
    }

    @Override
    public byte[] getRawData() {
        return new byte[]{(byte)0x80,HEADER,operationMode,forceClearList};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
