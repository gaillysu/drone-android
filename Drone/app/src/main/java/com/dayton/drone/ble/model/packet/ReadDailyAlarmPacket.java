package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.DailyAlarmModel;
import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;

import java.util.ArrayList;
import java.util.List;

public class ReadDailyAlarmPacket extends DronePacket{
    private  final static int MAXCOUNT = 5;
    private  List<DailyAlarmModel> entries;
    public ReadDailyAlarmPacket(List<MEDRawData> packets) {
        super(packets);
    }

    public List<DailyAlarmModel> getEntries()
    {
      //due to Alarm count is up to 5, so we can save it in one packet (limited 20 bytes)
      int count = getPackets().get(0).getRawData()[2]>MAXCOUNT?MAXCOUNT:getPackets().get(0).getRawData()[2];
      entries = new ArrayList<>(count);
      for(int i=0;i<count;i++){
          DailyAlarmModel dailyAlarmModel = new DailyAlarmModel(getPackets().get(0).getRawData()[3+i*3],getPackets().get(0).getRawData()[4+i*3],getPackets().get(0).getRawData()[5+i*3]);
          entries.add(dailyAlarmModel);
      }
      return entries;
    }

}
