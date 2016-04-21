package com.dayton.drone.ble.model.request.init;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

import java.util.Date;

/**
 * Created by med on 16/4/13.
 */
public class SetSystemConfig extends RequestBase{
    public final static byte HEADER = (byte)0x0F;
    final int clockFormat;
    final int sleepMode ;
    final long sleepAutoStartTime ;
    final long sleepAutoEndTime ;

    public SetSystemConfig(Context context, int clockFormat, int sleepMode, long sleepAutoStartTime, long sleepAutoEndTime) {
        super(context);
        this.clockFormat = clockFormat;
        this.sleepMode = sleepMode;
        this.sleepAutoStartTime = sleepAutoStartTime;
        this.sleepAutoEndTime = sleepAutoEndTime;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][] {
                {(byte) 0x80,HEADER,0x08,(byte)clockFormat,0x01,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {(byte) 0x80,HEADER,0x04,0x01,0x01,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
                {(byte) 0x80,HEADER,0x09,0x05,(byte)sleepMode,(byte)(sleepAutoStartTime&0xFF),
                        (byte)((sleepAutoStartTime>>8)&0xFF),
                        (byte)(sleepAutoEndTime&0xFF),
                        (byte)((sleepAutoEndTime>>8)&0xFF),
                        0,0,0,0,0,0,0,0,0,0,0}
        };
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
