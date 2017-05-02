package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 17/5/2.
 */

public class DisableUrbanNavigation extends RequestBase {
    public final static byte HEADER = (byte)0x35;
    protected DisableUrbanNavigation(Context context) {
        super(context);
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
