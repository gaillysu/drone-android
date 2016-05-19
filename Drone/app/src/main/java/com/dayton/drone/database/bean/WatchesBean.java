package com.dayton.drone.database.bean;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by med on 16/5/19.
 */
public class WatchesBean {
    /**
     * field name and initialize value, Primary field, it is a local database value
     */
    public static final String fID = "id";
    @DatabaseField(id = true)
    private int id = (int) Math.floor(Math.random()*Integer.MAX_VALUE);

    /**
     * return from "watch API"
     */
    public static final String fWatchID = "watchID";
    @DatabaseField
    private int watchID;

    /**
     * which user ID
     */
    public static final String fUserID = "userID";
    @DatabaseField
    private String userID;

    /**
     * serial is the watch unique ID
     */
    public static final String fSerialNumber = "serialNumber";
    @DatabaseField
    private String serialNumber;

    /**
     * MAC address is the ble ID
     */
    public static final String fMacAddress = "macAddress";
    @DatabaseField
    private String macAddress;

    /**
     * the model type of the watch, to show the name and icon
     */
    public static final String fModelName = "modelName";
    @DatabaseField
    private String modelName;


    /**
     * the watch firmware version
     */
    public static final String fFirmwareVersion = "firmwareVersion";
    @DatabaseField
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
