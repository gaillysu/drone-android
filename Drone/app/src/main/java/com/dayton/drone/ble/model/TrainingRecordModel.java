package com.dayton.drone.ble.model;

/**
 * Created by med on 16/4/22.
 */
public class TrainingRecordModel {

    /**
     * SUMMARY HEADER 32 bytes
     */
    private int trainingStartStamp; //Unit: "s", if use "ms", it shall use "long" type save it
    private int trainingDuration;
    private int trainingDistance;
    //dummy 20 bytes

    private short sampleDataCount;
    private byte [] sampleData; //Sets of 8 bytes data
    private short[] sampleSpeed;
    private byte statusFIFO;


    public int getTrainingStartStamp() {
        return trainingStartStamp;
    }

    public void setTrainingStartStamp(int trainingStartStamp) {
        this.trainingStartStamp = trainingStartStamp;
    }

    public int getTrainingDuration() {
        return trainingDuration;
    }

    public void setTrainingDuration(int trainingDuration) {
        this.trainingDuration = trainingDuration;
    }

    public int getTrainingDistance() {
        return trainingDistance;
    }

    public void setTrainingDistance(int trainingDistance) {
        this.trainingDistance = trainingDistance;
    }

    public short getSampleDataCount() {
        return sampleDataCount;
    }

    public void setSampleDataCount(short sampleDataCount) {
        this.sampleDataCount = sampleDataCount;
    }

    public byte[] getSampleData() {
        return sampleData;
    }

    public void setSampleData(byte[] sampleData) {
        this.sampleData = sampleData;
    }


    public byte getStatusFIFO() {
        return statusFIFO;
    }

    public void setStatusFIFO(byte statusFIFO) {
        this.statusFIFO = statusFIFO;
    }

    public short[] getSampleSpeed() {
        return sampleSpeed;
    }

    public void setSampleSpeed(short[] sampleSpeed) {
        this.sampleSpeed = sampleSpeed;
    }
}
