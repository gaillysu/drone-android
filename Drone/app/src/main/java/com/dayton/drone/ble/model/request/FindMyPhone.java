package com.dayton.drone.ble.model.request;


import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 17/5/2.
 */

public class FindMyPhone extends RequestBase {
    public final static byte HEADER = (byte)0x36;

    private byte enableSound;

    protected FindMyPhone(Context context,int enableSound) {
        super(context);
        this.enableSound = (byte)enableSound;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER, enableSound,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
