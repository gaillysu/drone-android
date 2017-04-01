package com.dayton.drone.model;


/**
 * Created by med on 16/5/19.
 */
public class Watches {

    private int id = (int) Math.floor(Math.random()*Integer.MAX_VALUE);
    private int watchID;
    private String userID;
    private String serialNumber;
    private String macAddress;
    private String modelName;
    private String firmwareVersion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWatchID() {
        return watchID;
    }

    public void setWatchID(int watchID) {
        this.watchID = watchID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }
}

