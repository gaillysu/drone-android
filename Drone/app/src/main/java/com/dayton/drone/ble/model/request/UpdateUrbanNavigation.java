package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.ble.util.SplitPacketConverter;

/**
 * Created by med on 17/5/2.
 */

public class UpdateUrbanNavigation extends RequestBase {
    public final static byte HEADER = (byte)0x35;

    private long latitude; //in 1e-7 deg
    private long longitude; //in 1e-7 deg
    private long distance;  //in "meter"

    public UpdateUrbanNavigation(Context context, long latitude, long longitude, long distance) {
        super(context);
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    @Override
    public byte[][] getRawDataEx() {
        return new byte[][]{{(byte) 0x80,HEADER,2,
                12,
                (byte) (latitude&0xFF),
                (byte) (latitude>>8&0xFF),
                (byte) (latitude>>16&0xFF),
                (byte) (latitude>>24&0xFF),
                (byte) (longitude&0xFF),
                (byte) (longitude>>8&0xFF),
                (byte) (longitude>>16&0xFF),
                (byte) (longitude>>24&0xFF),
                (byte) (distance&0xFF),
                (byte) (distance>>8&0xFF),
                (byte) (distance>>16&0xFF),
                (byte) (distance>>24&0xFF),
                0,0,0,0}};
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
