package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;


public class ReadCountdownTimerRequest extends RequestBase {
    public final static byte HEADER = (byte)0x3A;
    public ReadCountdownTimerRequest(Context context) {
        super(context);
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{
                {
                        (byte) 0x80,HEADER,0
                }
        };
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}