package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.datasource.GattAttributesDataSourceImpl;

import java.util.Date;

/**
 * Created by med on 16/4/12.
 */
public class SetRTCRequest extends RequestBase {
    public final static byte HEADER = (byte)0x03;

    public SetRTCRequest(Context context) {
        super(new GattAttributesDataSourceImpl(context));
    }

    @Override
    public byte[] getRawData() {
        Date theDay = new Date();
        int  timezone =  theDay.getTimezoneOffset()/15;
        long timestamp = theDay.getTime()-theDay.getTimezoneOffset()*60;
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
