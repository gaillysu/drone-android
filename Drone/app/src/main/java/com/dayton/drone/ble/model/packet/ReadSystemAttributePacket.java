package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.packet.base.DronePacket;
import com.dayton.drone.ble.util.Constants;

import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.HexUtils;

import org.apache.commons.codec.Charsets;

import java.util.List;

/**
 * Created by med on 17/08/15.
 */
public class ReadSystemAttributePacket extends DronePacket{
    public ReadSystemAttributePacket(List<MEDRawData> packets) {
        super(packets);
    }

    /**
     *
     *return firmware version as string,for example:"0.09", or a blank String object
     */
    public String getFirmwareVersion(){
        if(getPackets().get(0).getRawData()[2] == Constants.SystemAttributeID.FirmwareVersion.rawValue()) {
            int length = getPackets().get(0).getRawData()[3];
            byte[] bytes = new byte[length];
            System.arraycopy(getPackets().get(0).getRawData(),4,bytes,0,length);
            return new String(bytes, Charsets.US_ASCII);
        }
        return new String();
    }

    /**
     *
     * @return watch model ID
     */
    public int getModelID() {
        if(getPackets().get(0).getRawData()[2] == Constants.SystemAttributeID.ServiceData.rawValue()) {
            byte[] bytes = new byte[]{getPackets().get(0).getRawData()[6],getPackets().get(0).getRawData()[7]};
            return HexUtils.bytesToInt(bytes);
        }
        return -1;
    }

    /**
     *
     * @return watch vendor ID
     */
    public int getVendorID() {
        if(getPackets().get(0).getRawData()[2] == Constants.SystemAttributeID.ServiceData.rawValue()) {
            byte[] bytes = new byte[]{getPackets().get(0).getRawData()[4],getPackets().get(0).getRawData()[5]};
            return HexUtils.bytesToInt(bytes);
        }
        return -1;
    }
}
