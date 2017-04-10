package com.dayton.drone.ble.model.request.init;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;

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
    final Constants.SystemConfigID id;

    public SetSystemConfig(Context context, int clockFormat, int sleepMode, long sleepAutoStartTime, long sleepAutoEndTime, Constants.SystemConfigID id) {
        super(context);
        this.clockFormat = clockFormat;
        this.sleepMode = sleepMode;
        this.sleepAutoStartTime = sleepAutoStartTime;
        this.sleepAutoEndTime = sleepAutoEndTime;
        this.id = id;
    }

    @Override
    public byte[][] getRawDataEx() {
        if(id == Constants.SystemConfigID.ClockFormat)
        {
            return new byte[][]{{(byte) 0x80,HEADER, (byte) id.rawValue(),(byte)clockFormat,0x01,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
        }
        else if(id == Constants.SystemConfigID.Enabled)
        {
            return new byte[][]{{(byte) 0x80,HEADER,(byte) id.rawValue(),0x01,0x01,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
        }
        else if(id == Constants.SystemConfigID.SleepConfig)
        {
            return new byte[][]{{(byte) 0x80,HEADER,(byte) id.rawValue(),0x05,(byte)sleepMode,(byte)(sleepAutoStartTime&0xFF),
                    (byte)((sleepAutoStartTime>>8)&0xFF),
                    (byte)(sleepAutoEndTime&0xFF),
                    (byte)((sleepAutoEndTime>>8)&0xFF),
                    0,0,0,0,0,0,0,0,0,0,0}};
        }
        else if(id == Constants.SystemConfigID.CompassAutoOnDuration)
        {
            return new byte[][]{{(byte) 0x80,HEADER,(byte) id.rawValue(),0x02,0x01,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
        }
        else if(id == Constants.SystemConfigID.TopKeyCustomization)
        {
            return new byte[][]{{(byte) 0x80,HEADER,(byte) id.rawValue(),0x01,0x01,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
        }
        return null;
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
