package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.HexUtils;

import java.util.List;

/**
 * Created by med on 16/4/14.
 */
public class GetStepsGoalPacket extends DronePacket {
    public GetStepsGoalPacket(List<MEDRawData> packets) {
        super(packets);
    }

    public int getGoal() {
        int dailyStepGoal = 0;
        dailyStepGoal  = HexUtils.bytesToInt(new byte[]{getPackets().get(0).getRawData()[2],
                getPackets().get(0).getRawData()[3],
                getPackets().get(0).getRawData()[4],
                getPackets().get(0).getRawData()[5]});

        return dailyStepGoal;
    }
    public int getSteps() {
        int dailySteps = 0;
        dailySteps = HexUtils.bytesToInt(new byte[]{getPackets().get(0).getRawData()[7],
                getPackets().get(0).getRawData()[8],
                getPackets().get(0).getRawData()[9],
                getPackets().get(0).getRawData()[10]});
        return dailySteps;
     }
}
