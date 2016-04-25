package com.dayton.drone.ble.model.request.dfu;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/25.
 */
public class SetDFUModeRequest extends RequestBase {
    public final static byte HEADER = (byte)0x70;
    final private byte dfuMode;

    /**
     *
     * @param context Application Context
     * @param dfuMode 0x01: BLE DFU mode; 0x02: MCU DFU mode
     */
    public SetDFUModeRequest(Context context, byte dfuMode) {
        super(context);
        this.dfuMode = dfuMode;
    }

    @Override
    public byte[] getRawData() {
        return new byte[]{(byte)0x80,HEADER,(byte)0xD4,(byte)0x17,(byte)0xA6,(byte)0x84,dfuMode};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
