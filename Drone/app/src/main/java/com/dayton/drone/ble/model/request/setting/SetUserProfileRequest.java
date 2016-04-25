package com.dayton.drone.ble.model.request.setting;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;

/**
 * Created by med on 16/4/13.
 */
public class SetUserProfileRequest extends RequestBase{
    public final static byte HEADER = (byte)0x31;

    private int mWeight = 77;//kg, NOTICE: this value is sent to watch, need have multiple of 10, 77==>770
    private int mHeight = 175; //cm
    private int mGender = 1; //1: Male; 0:Female
    private int mStridelength = 70 ; //cm


    public SetUserProfileRequest(Context context) {
        super(context);
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][] {
                {(byte) 0x80,HEADER,
                        (byte)((mWeight*10)&0xFF),
                        (byte)(((mWeight*10)>>8)&0xFF),
                        (byte)(mHeight&0xFF),
                        (byte)(mGender&0xFF),
                        (byte)(mStridelength&0xFF)}
        };
    }
}
