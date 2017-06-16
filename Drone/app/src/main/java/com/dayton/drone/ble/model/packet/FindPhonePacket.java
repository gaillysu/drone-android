package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;

import java.util.List;


public class FindPhonePacket extends DronePacket{
    public final static byte HEADER = (byte)0x36;
    public FindPhonePacket(List<MEDRawData> packets) {
        super(packets);
    }

    /**
     *
     * @return 1 = Enable the sound,0 = Disable the sound
     */
    public byte getFindPhoneOperation() {
        return getPackets().get(0).getRawData()[2];
    }

}
