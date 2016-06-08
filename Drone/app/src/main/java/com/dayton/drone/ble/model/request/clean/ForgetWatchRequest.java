package com.dayton.drone.ble.model.request.clean;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/25.
 * this class used for drop the connection and unpair watch
 */
public class ForgetWatchRequest extends RequestBase {
    private final static byte HEADER = (byte)0x23;
    public ForgetWatchRequest(Context context) {
        super(context);
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }

    @Override
    public byte[] getRawData() {
        return new byte[]{(byte) 0x80,HEADER};
    }
}
