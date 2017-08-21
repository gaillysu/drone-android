package com.dayton.drone.ble.model.request.ancssetting;


import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;


public class SetCategoryPriorityRequest extends RequestBase {
    public final static byte HEADER = (byte)0x1C;

    //Category as defined in ANCS specification
    private byte category;
    //Priority (0-5; 0=Silent; 1-5=patterns)
    private byte priority;

    protected SetCategoryPriorityRequest(Context context, int category,int priority) {
        super(context);
        this.category = (byte)category;
        this.priority = (byte)priority;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER, category,priority}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
