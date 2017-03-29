package com.dayton.drone.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by med on 16/5/19.
 */
public class Watches extends RealmObject {
    /**
     * field name and initialize value, Primary field, it is a local database value
     */
    @Ignore
    public static final String fID = "id";
    private int id = (int) Math.floor(Math.random()*Integer.MAX_VALUE);

    /**
     * return from "watch API"
     */
    @Ignore
    public static final String fWatchID = "watchID";
    private int watchID;

    /**
     * which user ID
     */
    @Ignore
    public static final String fUserID = "userID";
    private String userID;

    /**
     * serial is the watch unique ID
     */
    @Ignore
    public static final String fSerialNumber = "serialNumber";
    private String serialNumber;

    /**
     * MAC address is the ble ID
     */
    @Ignore
    public static final String fMacAddress = "macAddress";
    private String macAddress;

    /**
     * the model type of the watch, to show the name and icon
     */
    @Ignore
    public static final String fModelName = "modelName";
    private String modelName;


    /**
     * the watch firmware version
     */
    @Ignore
    public static final String fFirmwareVersion = "firmwareVersion";
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

