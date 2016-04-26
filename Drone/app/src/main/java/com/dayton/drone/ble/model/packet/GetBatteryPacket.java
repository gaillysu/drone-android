package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;

import java.util.List;

/**
 * Created by med on 16/4/25.
 */
public class GetBatteryPacket extends DronePacket{
    public GetBatteryPacket(List<MEDRawData> packets) {
        super(packets);
    }

    /**
     *
     * @return battery status, 0x00: in use, 0x01: charging, 0x02: damaged, 0x03: calulating
     */
    public byte getBatteryStatus(){
        return getPackets().get(0).getRawData()[2];
    }

    /**
     *
     * @return battery level percentage
     */
    public byte getBatteryLevel() {
        return getPackets().get(0).getRawData()[3];
    }

}
