package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;

import java.util.List;

/**
 * Created by med on 16/4/14.
 */
public class SystemEventPacket extends DronePacket {
    public final static byte HEADER = (byte)0x02;
    public SystemEventPacket(List<MEDRawData> packets) {
        super(packets);
    }

    /**
     *
     * @return system event, pls refer to @ble.util.Constants.SystemEvent
     */
    public int getEvent() {
        return (int) getPackets().get(0).getRawData()[2];
    }
}
