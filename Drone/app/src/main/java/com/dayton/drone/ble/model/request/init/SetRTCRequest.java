package com.dayton.drone.ble.model.request.init;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

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
    public byte[] getRawData() {
        Date theDay = new Date();
        int  timezone = TimeZone.getDefault().getRawOffset()/1000/3600 * 15;
        long timestamp = theDay.getTime()/1000 + 3600 * 2;
        return new byte[] {
                        (byte)0x80,HEADER,
                        (byte) (timestamp&0xFF),
                        (byte) ((timestamp>>8)&0xFF),
                        (byte) (timestamp&0xFF),
                        (byte) (timestamp&0xFF),
                        (byte) (timezone&0xFF)
                };


    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
