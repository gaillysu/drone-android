package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.TimeZoneModel;
import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by med on 16/4/22.
 */
public class GetWorldClockPacket extends DronePacket {

    public GetWorldClockPacket(List<MEDRawData> packets) {
        super(packets);
    }

    public List<TimeZoneModel> getWorldClockTimeZone() {
        List<TimeZoneModel> timeZoneList = new ArrayList<>();
        int postionTimeZone = 0;
        for(int i=0;i<getPackets().get(0).getRawData()[2];i++)
        {
            if(i == 0)
            {
                postionTimeZone = 3;
            }
            else
            {
                postionTimeZone = postionTimeZone + timeZoneList.get(timeZoneList.size()-1).getTimeZoneBytesCount();
            }
            int timeZone = getPackets().get(0).getRawData()[postionTimeZone]/15;
            int timeZoneLength = getPackets().get(0).getRawData()[postionTimeZone+1];
            byte[] zoneName = new byte[timeZoneLength];
            System.arraycopy(getPackets().get(0).getRawData(),postionTimeZone+2,zoneName,0,timeZoneLength);
            timeZoneList.add(new TimeZoneModel(timeZone,new String(zoneName)));
        }
        return timeZoneList;
    }
}
