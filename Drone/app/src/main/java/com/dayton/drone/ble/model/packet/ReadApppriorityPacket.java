package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.HexUtils;

import java.util.List;


public class ReadApppriorityPacket extends DronePacket{
    public final static byte HEADER = (byte)0x1F;
    public ReadApppriorityPacket(List<MEDRawData> packets) {
        super(packets);
    }

    public short getNumberAPPpriority() {
        return (short)HexUtils.bytesToInt(new byte[]{getPackets().get(0).getRawData()[2],
                getPackets().get(0).getRawData()[3]});
    }

    public int getHash() {
        return HexUtils.bytesToInt(new byte[]{getPackets().get(0).getRawData()[4],
                getPackets().get(0).getRawData()[5],
                getPackets().get(0).getRawData()[6],
                getPackets().get(0).getRawData()[7]
        });
    }

}
