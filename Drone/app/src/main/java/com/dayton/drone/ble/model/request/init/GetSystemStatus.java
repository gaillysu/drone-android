package com.dayton.drone.ble.model.request.init;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/13.
 */
public class GetSystemStatus extends RequestBase {

    public final static byte HEADER = (byte)0x01;

    public GetSystemStatus(Context context) {
        super(context);
    }


    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{
                {(byte)0x80,HEADER}
        };
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
