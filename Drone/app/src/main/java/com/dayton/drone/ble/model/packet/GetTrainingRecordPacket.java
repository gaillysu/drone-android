package com.dayton.drone.ble.model.packet;

import com.dayton.drone.ble.model.TrainingRecordModel;
import com.dayton.drone.ble.model.packet.base.DronePacket;

import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.HexUtils;

import java.util.List;

/**
 * Created by med on 16/4/22.
 */
public class GetTrainingRecordPacket  extends DronePacket{
    public GetTrainingRecordPacket(List<MEDRawData> packets) {
        super(packets);
    }

    public TrainingRecordModel getTrainingRecord(){

        TrainingRecordModel trainingRecordModel = new TrainingRecordModel();

        trainingRecordModel.setTrainingStartStamp(HexUtils.bytesToInt(new byte[]{
                getPackets().get(0).getRawData()[2],
                getPackets().get(0).getRawData()[3],
                getPackets().get(0).getRawData()[4],
                getPackets().get(0).getRawData()[5]}));
        trainingRecordModel.setTrainingDuration(HexUtils.bytesToInt(new byte[]{
                getPackets().get(0).getRawData()[6],
                getPackets().get(0).getRawData()[7],
                getPackets().get(0).getRawData()[8],
                getPackets().get(0).getRawData()[9]}));
        trainingRecordModel.setTrainingDistance(HexUtils.bytesToInt(new byte[]{
                getPackets().get(0).getRawData()[10],
                getPackets().get(0).getRawData()[11],
                getPackets().get(0).getRawData()[12],
                getPackets().get(0).getRawData()[13]}));

        trainingRecordModel.setSampleDataCount((short) HexUtils.bytesToInt(new byte[]{
                getPackets().get(0).getRawData()[14],
                getPackets().get(0).getRawData()[15]}));

        byte[] dataSample = new  byte[trainingRecordModel.getSampleDataCount()*8];

        System.arraycopy(getPackets().get(0).getRawData(),16,dataSample,0,dataSample.length);
        trainingRecordModel.setSampleData(dataSample);

        short[] sampleSpeed = new short[trainingRecordModel.getSampleDataCount()];

        for(int i=0;i<dataSample.length;i=i+8)
        {
            sampleSpeed[i/8] = (short) HexUtils.bytesToInt(new byte[]{
                    dataSample[i],
                    dataSample[i+1]});
        }
        trainingRecordModel.setSampleSpeed(sampleSpeed);
        trainingRecordModel.setStatusFIFO(getPackets().get(0).getRawData()[32+2+dataSample.length]);

        return trainingRecordModel;
    }
}
