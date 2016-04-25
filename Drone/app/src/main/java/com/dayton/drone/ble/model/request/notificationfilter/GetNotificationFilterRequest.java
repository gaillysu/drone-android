package com.dayton.drone.ble.model.request.notificationfilter;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/22.
 */
public class GetNotificationFilterRequest extends RequestBase {
    public final static byte HEADER = (byte)0x17;

    public GetNotificationFilterRequest(Context context) {
        super(context);
    }

    @Override
    public byte[] getRawData() {
        return new byte[]{(byte)0x80,HEADER};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
