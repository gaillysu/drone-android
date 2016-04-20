package com.dayton.drone.ble.model.request.init;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

import java.util.Date;

/**
 * Created by med on 16/4/13.
 */
public class SetSystemConfig extends RequestBase{
    public final static byte HEADER = (byte)0x0F;
    public SetSystemConfig(Context context) {
        super(context);
    }

    @Override
    public byte[][] getRawDataEx() {
        long startTimestamp = new Date().getTime()/1000;
        long endTimestamp = startTimestamp + 24 * 60 *60;

        return new byte[][] {
                {(byte) 0x80,HEADER,0x08,0x01,0x01,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {(byte) 0x80,HEADER,0x04,0x01,0x01,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {(byte) 0x80,HEADER,0x09,0x05,0x01,(byte)(startTimestamp&0xFF),
                        (byte)((startTimestamp>>8)&0xFF),
                        (byte)(endTimestamp&0xFF),
                        (byte)((endTimestamp>>8)&0xFF),
                        0,0,0,0,0,0,0,0,0,0,0}
        };
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
