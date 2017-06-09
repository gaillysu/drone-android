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
    int clockFormat;
    int sleepMode ;
    long sleepAutoStartTime ;
    long sleepAutoEndTime ;
    Constants.SystemConfigID id;
    private short compassAutoOnDuration;
    private byte  topkeyFunction;
    private byte analogHandsConfig;

    public SetSystemConfig(Context context,Constants.SystemConfigID id) {
        super(context);
        this.id = id;
    }
    public SetSystemConfig(Context context, int sleepMode, long sleepAutoStartTime, long sleepAutoEndTime, Constants.SystemConfigID id) {
        this(context,id);
        this.sleepMode = sleepMode;
        this.sleepAutoStartTime = sleepAutoStartTime;
        this.sleepAutoEndTime = sleepAutoEndTime;
        this.id = id;
    }

    public SetSystemConfig(Context context, short value,Constants.SystemConfigID id) {
        this(context,id);
        if(id == Constants.SystemConfigID.CompassAutoOnDuration) {
            this.compassAutoOnDuration = value;
        }
    }

    public SetSystemConfig(Context context, byte value,Constants.SystemConfigID id) {
        this(context,id);
        if(id == Constants.SystemConfigID.ClockFormat) {
            this.clockFormat = value;
        }
        if(id == Constants.SystemConfigID.AnalogHandsConfig) {
            this.analogHandsConfig = value;
        }
        if(id == Constants.SystemConfigID.TopKeyCustomization) {
            this.topkeyFunction = value;
        }
    }

    @Override
    public byte[][] getRawDataEx() {
        if(id == Constants.SystemConfigID.ClockFormat)
        {
            return new byte[][]{{(byte) 0x80,HEADER, (byte) id.rawValue(),0x01,(byte)clockFormat,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
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
            return new byte[][]{{(byte) 0x80,HEADER,(byte) id.rawValue(),0x02,
                    (byte)(compassAutoOnDuration&0xFF),
                    (byte)((compassAutoOnDuration>>8)&0xFF),
                    0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
        }
        else if(id == Constants.SystemConfigID.TopKeyCustomization)
        {
            return new byte[][]{{(byte) 0x80,HEADER,(byte) id.rawValue(),0x01,(byte)topkeyFunction,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
        }
        else if(id == Constants.SystemConfigID.AnalogHandsConfig)
        {
            return new byte[][]{{(byte) 0x80,HEADER,(byte) id.rawValue(),0x01,analogHandsConfig,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
        }

        return null;
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
