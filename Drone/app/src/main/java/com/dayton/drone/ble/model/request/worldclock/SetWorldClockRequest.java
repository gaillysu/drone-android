package com.dayton.drone.ble.model.request.worldclock;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/13.
 */
public class SetWorldClockRequest extends RequestBase{
    public final static byte HEADER = (byte)0x06;

    int timeZoneCount;
    long timeStamp;
    String timeZoneName;

    public SetWorldClockRequest(Context context,int timeZoneCount, long timeStamp,String timeZoneName) {
        super(context);
        this.timeZoneCount = timeZoneCount;
        this.timeStamp = timeStamp;
        this.timeZoneName = timeZoneName;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{
                {
                        //TODO :fill this packet by lastest spec.
                        (byte) 0x80,HEADER
                }
        };
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
