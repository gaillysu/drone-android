package com.dayton.drone.ble.model.request.init;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;

/**
 * Created by med on 16/4/13.
 */
public class SetAppConfigRequest extends RequestBase{
    public final static byte HEADER = (byte)0x04;
    private final Constants.ApplicationID id;
    public SetAppConfigRequest(Context context, Constants.ApplicationID id) {
        super(context);
        this.id = id;
    }
    @Override
    public byte getHeader() {
        return HEADER;
    }
    @Override
    public byte[][] getRawDataEx() {
        return new byte[][] {
                {(byte) 0x80,HEADER, (byte) id.rawValue(),1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}
        };
    }
}
