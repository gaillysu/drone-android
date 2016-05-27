package com.dayton.drone.ble.model.request.setting;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.model.User;

/**
 * Created by med on 16/4/13.
 */
public class SetUserProfileRequest extends RequestBase{
    public final static byte HEADER = (byte)0x31;

    private double weight;//in "kg", NOTICE: this value is sent to watch, need have multiple of 10, 77==>770
    private int height; //in "cm"
    private int gender; //1: Male; 0:Female
    private int strideLength; //in "cm"

    public SetUserProfileRequest(Context context, User user) {
        super(context);
        weight = user.getWeight();
        height = user.getHeight();
        gender = user.getGender();
        strideLength = user.getStrideLength();
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][] {
                {(byte) 0x80,HEADER,
                        (byte)((int)(weight *10)&0xFF),
                        (byte)(((int)(weight *10)>>8)&0xFF),
                        (byte)(height &0xFF),
                        (byte)(gender &0xFF),
                        (byte)(strideLength &0xFF)}
        };
    }
}
