package com.dayton.drone.ble.model.request.worldclock;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/13.
 */
public class GetWorldClockRequest extends RequestBase {
    public final static byte HEADER = (byte)0x07;
    public GetWorldClockRequest(Context context) {
        super(context);
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
