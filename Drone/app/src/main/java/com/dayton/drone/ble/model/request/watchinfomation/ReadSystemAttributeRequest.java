package com.dayton.drone.ble.model.request.watchinfomation;


import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 17/08/15.
 * return: firmware version, watch model ID and watch vendor ID
 * pls refer to response packets @ReadSystemAttributePacket
 */

public class ReadSystemAttributeRequest extends RequestBase {
    public final static byte HEADER = (byte)0x15;

    private byte attributeID;

    protected ReadSystemAttributeRequest(Context context, int attributeID) {
        super(context);
        this.attributeID = (byte)attributeID;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER, attributeID,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
