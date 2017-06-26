package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.model.DailyAlarmModel;
import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.ble.util.SplitPacketConverter;

import java.util.ArrayList;
import java.util.List;


public class SetDailyAlarmRequest extends RequestBase{
    public final static byte HEADER = (byte)0x37;
    private  List<DailyAlarmModel> entries;
    public SetDailyAlarmRequest(Context context, List<DailyAlarmModel> entries) {
        super(context);
        this.entries = entries;
    }

    @Override
    public byte[][] getRawDataEx() {
        int count = Math.min(DailyAlarmModel.MAXENTRY,entries.size());
        List<Byte> data = new ArrayList<>();
        data.add(HEADER);
        data.add((byte) count);
        for(int i=0;i<count;i++)
        {
            data.add((byte)(entries.get(i).getHour()));
            data.add((byte) entries.get(i).getMinute());
            data.add((byte) entries.get(i).getStatus());
        }
        byte[] rawData = new byte[data.size()];
        for(int i=0;i<data.size();i++){
            rawData[i] = data.get(i).byteValue();
        }
        return SplitPacketConverter.rawData2Packets(rawData,Constants.MTU);
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
