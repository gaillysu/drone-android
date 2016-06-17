package com.dayton.drone.ble.model.request.setting;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/6/17.
 */
public class SetStepsToWatchReuqest extends RequestBase {
    public final static byte HEADER = (byte)0x30;
    private final int steps;

    public SetStepsToWatchReuqest(Context context, int steps) {
        super(context);
        this.steps = steps;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{
                {
                        (byte) 0x80,HEADER,
                        (byte) (steps&0xFF),
                        (byte) (steps>>8&0xFF),
                        (byte) (steps>>16&0xFF),
                        (byte) (steps>>24&0xFF)
                }
        };
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
