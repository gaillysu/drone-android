package com.dayton.drone.ble.model.ancs;

/**
 * Created by med on 16/6/7.
 */
public class Sms extends NotificationDataSource {

    public Sms(int notificationID) {
        super(notificationID);
    }

    @Override
    public byte getAlertCategory() {
        return (byte)0xF2;
    }
}
