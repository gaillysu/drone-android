package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.HexUtils;

import java.util.List;

/**
 * Created by med on 16/4/25.
 */
public class GetUserProfilePacket extends DronePacket {
    public GetUserProfilePacket(List<MEDRawData> packets) {
        super(packets);
    }

    /**
     *
     * @return weight in "kg"
     */
    public short getWeight() {
        return (short)(HexUtils.bytesToInt(new byte[]{getPackets().get(0).getRawData()[2]
                         ,getPackets().get(0).getRawData()[3]})/10);
    }

    /**
     *
     * @return height in "cm"
     */
    public byte getHeight() {
        return getPackets().get(0).getRawData()[4];
    }

    /**
     *
     * @return ,1: male; 0: female
     */
    public byte getGender() {
        return getPackets().get(0).getRawData()[5];
    }

    /**
     *
     * @return average stride length in "cm"
     */
    public byte getStrideLength() {
        return getPackets().get(0).getRawData()[6];
    }
}
