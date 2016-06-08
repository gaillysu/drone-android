package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.HexUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by med on 16/4/14.
 */
public class ActivityPacket extends DronePacket {
    public ActivityPacket(List<MEDRawData> packets) {
        super(packets);
    }

    /**
     *
     * @return the remained record number
     */
    public  int getMore() {
        return (int) getPackets().get(0).getRawData()[8];
    }

    public  int getSteps() {
        int steps = 0;
        steps  = HexUtils.bytesToInt(new byte[]{getPackets().get(0).getRawData()[6],
                getPackets().get(0).getRawData()[7]});
        return steps;
    }

    public Date getDate() {
        int date = 0;
        date  = HexUtils.bytesToInt(new byte[]{getPackets().get(0).getRawData()[2],
                getPackets().get(0).getRawData()[3],
                getPackets().get(0).getRawData()[4],
                getPackets().get(0).getRawData()[5]});
        return new Date(date* 1000L);
    }
}
