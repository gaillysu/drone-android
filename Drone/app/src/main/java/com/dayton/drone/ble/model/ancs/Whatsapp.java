package com.dayton.drone.ble.model.ancs;

/**
 * Created by med on 16/6/7.
 */
public class Whatsapp extends NotificationDataSource {
    public Whatsapp(int notificationID) {
        super(notificationID);
    }

    @Override
    public byte getAlertCategory() {
        return (byte)0x0b;
    }
}
