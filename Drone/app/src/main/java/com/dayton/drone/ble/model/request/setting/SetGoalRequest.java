package com.dayton.drone.ble.model.request.setting;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/13.
 */
public class SetGoalRequest extends RequestBase {
    public final static byte HEADER = (byte)0x12;
    public final static int DEFAULTSTEPSGOAL = 10000;
    private final int stepsGoal;

    public SetGoalRequest(Context context, int stepsGoal) {
        super(context);
        this.stepsGoal = stepsGoal;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{
                {
                        (byte) 0x80,HEADER,
                        (byte) (stepsGoal&0xFF),
                        (byte) (stepsGoal>>8&0xFF),
                        (byte) (stepsGoal>>16&0xFF),
                        (byte) (stepsGoal>>24&0xFF),
                        (byte) (0)
                }
        };
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}