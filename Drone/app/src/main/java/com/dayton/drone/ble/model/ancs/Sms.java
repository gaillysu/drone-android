package com.dayton.drone.ble.model.ancs;

/**
 * Created by med on 16/6/7.
 */
public class Sms extends NotificationDataSource {
    @Override
    public int getAlertCategory() {
        return 0xF2;
    }
}
