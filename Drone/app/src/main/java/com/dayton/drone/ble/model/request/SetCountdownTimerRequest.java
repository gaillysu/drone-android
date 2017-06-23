package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;


public class SetCountdownTimerRequest extends RequestBase {
    public final static byte HEADER = (byte)0x39;
    private final static short COUNT_LIMITED = 1439;
    private final short countdownInMinutes; //0~1439 in "minutes"

    public SetCountdownTimerRequest(Context context, short countdownInMinutes) {
        super(context);
        this.countdownInMinutes = (countdownInMinutes>COUNT_LIMITED?COUNT_LIMITED:countdownInMinutes);
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{
                {
                        (byte) 0x80,HEADER,
                        (byte) (countdownInMinutes&0xFF),
                        (byte) (countdownInMinutes>>8&0xFF),
                        (byte) (0)
                }
        };
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}