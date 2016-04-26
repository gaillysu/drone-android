package com.dayton.drone.ble.model.request.init;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/13.
 */
public class SetAppConfigRequest extends RequestBase{
    public final static byte HEADER = (byte)0x04;
    public SetAppConfigRequest(Context context) {
        super(context);
    }
    @Override
    public byte getHeader() {
        return HEADER;
    }
    @Override
    public byte[][] getRawDataEx() {
        return new byte[][] {
                {(byte) 0x80,HEADER,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {(byte) 0x80,HEADER,2,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
        };
    }
}
