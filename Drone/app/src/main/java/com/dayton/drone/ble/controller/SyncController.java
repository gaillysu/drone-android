package com.dayton.drone.ble.controller;

import net.medcorp.library.ble.model.request.BLERequestData;

/**
 * Created by med on 16/4/11.
 * this  interface give all functions that communication with watch.
 */
public interface SyncController {

    void startConnect(boolean forceScan);
    boolean isConnected();

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

}
