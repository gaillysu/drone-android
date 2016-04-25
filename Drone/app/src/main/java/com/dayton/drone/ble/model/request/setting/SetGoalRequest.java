package com.dayton.drone.ble.model.request.setting;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/13.
 */
public class SetGoalRequest extends RequestBase {
    public final static byte HEADER = (byte)0x12;
    public final static int DEFAULTSTEPSGOAL = 7000;
    private final int stepsGoal;
    private final int timeFrame = 0;//always 0x00
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
                        (byte) (timeFrame &0xFF)
                }
        };
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
