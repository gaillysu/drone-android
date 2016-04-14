package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;

import java.util.List;

/**
 * Created by med on 16/4/13.
 */
public class SystemStatusPacket extends DronePacket {
    public SystemStatusPacket(List<MEDRawData> packets) {
        super(packets);
    }

    /**
     *
     * @return watch status, @ble.util.Constants.SystemStatus
     */
    public  int getStatus() {
        return (int) getPackets().get(0).getRawData()[2];
    }
}
