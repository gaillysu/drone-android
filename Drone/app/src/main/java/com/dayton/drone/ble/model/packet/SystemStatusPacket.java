package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.HexUtils;

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
     * @return watch status, @ble.util.Constants.SystemStatus, refer to table 1: every bit has different meanings, from bit 0~8
     */
    public  int getStatus() {
        return HexUtils.bytesToInt(new byte[]{getPackets().get(0).getRawData()[2],getPackets().get(0).getRawData()[3]});
    }
}
