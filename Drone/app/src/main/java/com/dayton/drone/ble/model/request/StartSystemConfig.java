package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;

/**
 * Created by med on 16/4/13.
 */
public class StartSystemConfig extends RequestBase{
    public final static byte HEADER = (byte)0x34;
    final Constants.StartSystemSettingID id;
    final  int operation;

    public StartSystemConfig(Context context, int operation, Constants.StartSystemSettingID id) {
        super(context);
        this.id = id;
        this.operation = operation;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER, (byte) id.rawValue(),(byte)operation,0x01,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
