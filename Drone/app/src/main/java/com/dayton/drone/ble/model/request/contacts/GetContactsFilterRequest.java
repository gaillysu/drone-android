package com.dayton.drone.ble.model.request.contacts;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/13.
 */
public class GetContactsFilterRequest extends RequestBase {
    public final static byte HEADER = (byte)0x19;
    public GetContactsFilterRequest(Context context) {
        super(context);
    }

    @Override
    public byte[] getRawData() {
        return new byte[]{(byte)0x80,HEADER};
    }

    @Override
    public byte[][] getRawDataEx() {
        return null;
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
