package com.dayton.drone.ble.controller;

import net.medcorp.library.ble.model.request.BLERequestData;
import com.dayton.drone.ble.server.GattServerService;

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

    GattServerService getGattServerService();

}
