package com.dayton.drone.ble.model.request;

import android.content.Context;

import com.dayton.drone.ble.model.WeatherUpdateModel;
import com.dayton.drone.ble.model.request.base.RequestBase;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.ble.util.SplitPacketConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by med on 16/4/13.
 */
public class UpdateWeatherInfomationRequest extends RequestBase{
    public final static byte HEADER = (byte)0x26;
    private  List<WeatherUpdateModel> entries;
    public UpdateWeatherInfomationRequest(Context context, List<WeatherUpdateModel> entries) {
        super(context);
        this.entries = entries;
    }

    @Override
    public byte[][] getRawDataEx() {
        int count = Math.min(WeatherUpdateModel.MAXENTRY,entries.size());
        List<Byte> data = new ArrayList<>();
        data.add(HEADER);
        data.add((byte) count);
        for(int i=0;i<count;i++)
        {
            data.add((byte)(entries.get(i).getId()));
            data.add((byte) (entries.get(i).getTemperature()&0xFF));
            data.add((byte) (entries.get(i).getTemperature()>>8&0xFF));
            data.add((byte) entries.get(i).getStatus().rawValue());
        }
        byte[] rawData = new byte[data.size()];
        for(int i=0;i<data.size();i++){
            rawData[i] = data.get(i).byteValue();
        }
        return SplitPacketConverter.rawData2Packets(rawData, Constants.MTU);
    }

    @Override
    public byte getHeader() {
        return HEADER;
    }
}
