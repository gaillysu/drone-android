package com.dayton.drone.ble.model.request.ancssetting;


import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;


public class SetAppPriorityRequest extends RequestBase {
    public final static byte HEADER = (byte)0x1E;
    /**
     * <0x00> Do nothing
     <0x01> Clear all category priority to default
     */
    private byte clearall;

    protected SetAppPriorityRequest(Context context, int clearall) {
        super(context);
        this.clearall = (byte)clearall;

    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER, clearall}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
