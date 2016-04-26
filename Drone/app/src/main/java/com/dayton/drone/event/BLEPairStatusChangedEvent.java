package com.dayton.drone.event;

/**
 * Created by med on 16/4/19.
 */
public class BLEPairStatusChangedEvent {
    /**
     * status:BluetoothDevice.BOND_BONDED,BluetoothDevice.BOND_NONE
     */
    final int status;

    public BLEPairStatusChangedEvent(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
