package com.dayton.drone.ble.model.ancs;

/**
 * Created by med on 16/6/7.
 */
public class Facebook extends NotificationDataSource {
    public Facebook(int notificationID) {
        super(notificationID);
    }

    @Override
    public byte getAlertCategory() {
        return (byte)0x0b;
    }
}
