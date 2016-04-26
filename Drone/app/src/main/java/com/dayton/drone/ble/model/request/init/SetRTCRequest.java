package com.dayton.drone.ble.model.request.init;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by med on 16/4/12.
 */
public class SetRTCRequest extends RequestBase {
    public final static byte HEADER = (byte)0x03;

    public SetRTCRequest(Context context) {
        super(context);
    }

    @Override
    public byte[][] getRawDataEx() {
        Date theDay = new Date();
        int  timezone = TimeZone.getDefault().getRawOffset()/1000/3600 * 15;
        long timestamp = theDay.getTime()/1000 + 2*3600;
        return new byte[][]{
                {
                        (byte)0x80,HEADER,
                        (byte) (timestamp&0xFF),
                        (byte) ((timestamp>>8)&0xFF),
                        (byte) ((timestamp>>16)&0xFF),
                        (byte) ((timestamp>>24)&0xFF),
                        (byte) (timezone&0xFF)
                }
        };
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
