package com.dayton.drone.ble.controller;


/**
 * Created by med on 16/4/11.
 * this  interface give all functions that communication with watch.
 */
public interface SyncController {

    void startConnect(boolean forceScan);
    boolean isConnected();
    void disConnect();

    /**
     *
     * @return BLE firmware version
     */
    String getFirmwareVersion();

    /**
     *
     * @return MCU firmware version
     */
    String getSoftwareVersion();

    /**
     * unbind the saved watch MAC address
     */
    void forgetDevice();

    /**
     * find out which one watch is getting connected.
     */
    void findDevice();

    /**
     * read the battery status and level,the result will be delivered by event bus
     */
    void getBattery();

    void startNotificationListener();

    void calibrateWatch(int operation);

    void enableCompass(boolean enable);

    void setCompassAutoOnDuration(int duration);

    void startNavigation(double latitude,double longitude,String address);
    void stopNavigation();
    void updateNavigation(double latitude,double longitude,long distanceInMeters);
    void setHotKeyFunction(int functionId);
    void setCompassTimeout(int timeoutInseconds);
    void calibrateCompass(int operation);
    void setClockFormat(boolean format24Hour);
}
