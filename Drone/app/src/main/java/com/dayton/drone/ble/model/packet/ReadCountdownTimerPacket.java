package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.HexUtils;

import java.util.List;

public class ReadCountdownTimerPacket extends DronePacket{
    public ReadCountdownTimerPacket(List<MEDRawData> packets) {
        super(packets);
    }

    /**
     *
     * @return count down timer 's number, in minutes, up to 1439
     */
    public short getCountdownTimerInMinutes(){
        return (short)(HexUtils.bytesToInt(new byte[]{getPackets().get(0).getRawData()[2]
                ,getPackets().get(0).getRawData()[3]}));
    }
}
