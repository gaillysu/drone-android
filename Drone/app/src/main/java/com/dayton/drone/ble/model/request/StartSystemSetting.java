package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;

/**
 * Created by med on 16/4/13.
 */
public class StartSystemSetting extends RequestBase{
    public final static byte HEADER = (byte)0x34;
    final int id;
    final  int operation;

    public StartSystemSetting(Context context, int id, int operation) {
        super(context);
        this.id = id;
        this.operation = operation;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER, (byte) id,(byte)operation,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
