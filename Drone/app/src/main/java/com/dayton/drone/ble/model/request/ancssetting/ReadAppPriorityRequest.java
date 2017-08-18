package com.dayton.drone.ble.model.request.ancssetting;


import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;


public class ReadAppPriorityRequest extends RequestBase {
    public final static byte HEADER = (byte)0x1F;

    protected ReadAppPriorityRequest(Context context) {
        super(context);
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
