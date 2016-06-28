package com.dayton.drone.ble.model.ancs;

/**
 * Created by med on 16/6/7.
 */
public class Email extends NotificationDataSource {
    public Email(int notificationID) {
        super(notificationID);
    }

    @Override
    public byte getAlertCategory() {
        return 3;
    }
}
