package com.dayton.drone.ble.model.request;


import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.ble.util.SplitPacketConverter;


public class SetHapticsPatternsRequest extends RequestBase {
    public final static byte HEADER = (byte)0x3B;
    /**
     *
     * Haptics strength (0-100)
     */
    private byte strength;
    /**
     * Length of pattern #1 (1-10), disable null pattern
     */
    private byte[] pattern1;
    private byte[] pattern2;
    private byte[] pattern3;
    private byte[] pattern4;
    private byte[] pattern5;

    protected SetHapticsPatternsRequest(Context context, byte strength, byte[] pattern1, byte[] pattern2, byte[] pattern3, byte[] pattern4, byte[] pattern5) {
        super(context);
        this.strength = strength;
        this.pattern1 = pattern1;
        this.pattern2 = pattern2;
        this.pattern3 = pattern3;
        this.pattern4 = pattern4;
        this.pattern5 = pattern5;
    }

    @Override
    public byte[][] getRawDataEx() {
        byte[] rawData = new byte[7+pattern1.length+pattern2.length+pattern3.length+pattern4.length+pattern5.length];
        rawData[0] = HEADER;
        rawData[1] = strength;
        rawData[2] = (byte) (pattern1.length);
        System.arraycopy(pattern1,0,rawData,3,pattern1.length);
        rawData[3+pattern1.length] = (byte) (pattern2.length);
        System.arraycopy(pattern2,0,rawData,4+pattern1.length,pattern2.length);
        rawData[4+pattern1.length + pattern2.length] = (byte) (pattern3.length);
        System.arraycopy(pattern3,0,rawData,5+pattern1.length + pattern2.length,pattern3.length);
        rawData[5+pattern1.length + pattern2.length + pattern3.length] = (byte) (pattern4.length);
        System.arraycopy(pattern4,0,rawData,6+pattern1.length + pattern2.length + pattern3.length,pattern4.length);
        rawData[6+pattern1.length + pattern2.length + pattern3.length + pattern4.length] = (byte) (pattern5.length);
        System.arraycopy(pattern5,0,rawData,7+pattern1.length + pattern2.length + pattern3.length + pattern4.length,pattern5.length);
        return SplitPacketConverter.rawData2Packets(rawData, Constants.MTU);
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
