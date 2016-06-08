package com.dayton.drone.ble.model.packet.base;

import com.dayton.drone.ble.model.packet.ActivityPacket;
import com.dayton.drone.ble.model.packet.GetBatteryPacket;
import com.dayton.drone.ble.model.packet.GetStepsGoalPacket;
import com.dayton.drone.ble.model.packet.GetWorldClockPacket;
import com.dayton.drone.ble.model.packet.SystemEventPacket;
import com.dayton.drone.ble.model.packet.SystemStatusPacket;

import net.medcorp.library.ble.model.response.MEDRawData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by med on 16/4/12.
 */
public class DronePacket {

    private final List<MEDRawData> mPackets;

    public DronePacket(List<MEDRawData> packets)
    {
        //make sure the mPackets always has values during the DronePacket object life period
        mPackets = new ArrayList<MEDRawData>(packets.size());
        for(MEDRawData medRawData:packets)
        {
            mPackets.add(medRawData);
        }
    }

    public byte getHeader()
    {
        return mPackets.get(0).getRawData()[1];
    }
    protected List<MEDRawData> getPackets()
    {
        return mPackets;
    }

    public boolean isVaildPackets() {
        return (mPackets.size() >0 && mPackets.get(0).getRawData().length>=2 && mPackets.get(0).getRawData()[0] == (byte)0x80);
    }

    public SystemStatusPacket newSystemStatusPacket() {
        return new SystemStatusPacket(mPackets);
    }
    public SystemEventPacket newSystemEventPacket() {
        return new SystemEventPacket(mPackets);
    }
    public ActivityPacket newActivityPacket() {
        return new ActivityPacket(mPackets);
    }
    public GetStepsGoalPacket newGetStepsGoalPacket() {
        return new GetStepsGoalPacket(mPackets);
    }
    public GetWorldClockPacket newGetWorldClockPacket() {
        return new GetWorldClockPacket(mPackets);
    }
    public GetBatteryPacket newGetBatteryPacket(){
        return new GetBatteryPacket(mPackets);
    }
}
