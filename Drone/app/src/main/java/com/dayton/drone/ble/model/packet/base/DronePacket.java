package com.dayton.drone.ble.model.packet.base;

import com.dayton.drone.ble.model.packet.SystemStatusPacket;

import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.HexUtils;

import java.util.List;

/**
 * Created by med on 16/4/12.
 */
public class DronePacket {

    private final List<MEDRawData> mPackets;

    public DronePacket(List<MEDRawData> packets)
    {
        mPackets = packets;
    }

    public byte getHeader()
    {
        return mPackets.get(0).getRawData()[1];
    }
    public List<MEDRawData> getPackets()
    {
        return mPackets;
    }

    public boolean isVaildPackets() {
        return (mPackets.size() == 1 && mPackets.get(0).getRawData()[0] == (byte)0x80);
    }

    public SystemStatusPacket newSystemStatusPacket() {
        return new SystemStatusPacket(mPackets);
    }
}