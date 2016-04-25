package com.dayton.drone.ble.model.request.worldclock;

import android.content.Context;

import com.dayton.drone.ble.model.TimeZoneModel;
import com.dayton.drone.ble.model.request.base.RequestBase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by med on 16/4/13.
 */
public class SetWorldClockRequest extends RequestBase{
    public final static byte HEADER = (byte)0x06;

    List<TimeZoneModel> timeZoneList;

    public SetWorldClockRequest(Context context,List<TimeZoneModel> timeZoneList ) {
        super(context);
        this.timeZoneList = timeZoneList;
    }

    @Override
    public byte[][] getRawDataEx() {
        int timeZoneCount = timeZoneList.size();
        List<Byte> data = new ArrayList<>();
        data.add((byte) 0x80);
        data.add(HEADER);
        data.add((byte) timeZoneCount);
        for(int i=0;i<timeZoneCount;i++)
        {
            data.add((byte)(timeZoneList.get(i).getTimeZone()*15));
            data.add((byte) timeZoneList.get(i).getTimeZoneName().length());
            for(byte b:timeZoneList.get(i).getTimeZoneName().getBytes())
            {
                data.add(b);
            }
        }
        Byte[] dataByte = data.toArray(new Byte[data.size()]);
        byte [][]rawData = new byte[1][dataByte.length];
        for(int i=0;i<dataByte.length;i++) {
            rawData[0][i] = dataByte[i].byteValue();
        }
        return rawData;
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
