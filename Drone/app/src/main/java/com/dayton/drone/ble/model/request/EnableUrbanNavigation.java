package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.ble.util.SplitPacketConverter;

/**
 * Created by med on 17/5/2.
 */

public class EnableUrbanNavigation extends RequestBase {
    public final static byte HEADER = (byte)0x35;
    private final static int ADDRESS_LIMITED_LENGTH = 16;
    private long latitude; //in 1e-7 deg
    private long longitude; //in 1e-7 deg
    private String address;

    public EnableUrbanNavigation(Context context,long latitude,long longitude,String address) {
        super(context);
        this.latitude = latitude;
        this.longitude = longitude;
        if(address.length()>ADDRESS_LIMITED_LENGTH) {
            this.address = address.substring(0,ADDRESS_LIMITED_LENGTH);
        }
        else {
            this.address = address;
        }
    }

    @Override
    public byte[][] getRawDataEx() {
        byte[] rawData = new byte[12+address.length()];
        rawData[0] = HEADER;
        rawData[1] = 1;
        rawData[2] = (byte) (9 + address.length());
        rawData[3] = (byte) (latitude&0xFF);
        rawData[4] = (byte) ((latitude>>8)&0xFF);
        rawData[5] = (byte) ((latitude>>16)&0xFF);
        rawData[6] = (byte) ((latitude>>24)&0xFF);
        rawData[7] = (byte) (longitude&0xFF);
        rawData[8] = (byte) ((longitude>>8)&0xFF);
        rawData[9] = (byte) ((longitude>>16)&0xFF);
        rawData[10] = (byte) ((longitude>>24)&0xFF);
        rawData[11] = (byte) (address.length());
        System.arraycopy(address.getBytes(),0,rawData,12,address.length());
        return SplitPacketConverter.rawData2Packets(rawData, Constants.MTU);

    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
