package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.model.DailyAlarmModel;
import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.ble.util.SplitPacketConverter;

import java.util.ArrayList;
import java.util.List;


public class ReadDailyAlarmRequest extends RequestBase{
    public final static byte HEADER = (byte)0x38;

    public ReadDailyAlarmRequest(Context context) {
        super(context);
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER, 0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
