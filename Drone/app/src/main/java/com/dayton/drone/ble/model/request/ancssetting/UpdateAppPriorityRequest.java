package com.dayton.drone.ble.model.request.ancssetting;


import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.ble.util.SplitPacketConverter;


public class UpdateAppPriorityRequest extends RequestBase {
    public final static byte HEADER = (byte)0x20;
    /**
    <0x01> Add/Update, <0x02> Remove
     */
    private byte operation;
    private String appId;
    private byte priority;

    protected UpdateAppPriorityRequest(Context context, byte operation,String appId,byte priority) {
        super(context);
        this.operation = operation;
        this.appId = appId;
        this.priority = priority;
    }

    @Override
    public byte[][] getRawDataEx() {
        byte[] rawData = new byte[4+appId.length()];
        rawData[0] = HEADER;
        rawData[1] = operation;
        rawData[2] = (byte) (appId.length());
        System.arraycopy(appId.getBytes(),0,rawData,3,appId.length());
        rawData[3+appId.length()] = priority;
        return SplitPacketConverter.rawData2Packets(rawData, Constants.MTU);
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
