package com.dayton.drone.ble.model;


public class DailyAlarmModel {
    public final static int MAXENTRY = 5;
    final byte hour;
    final byte minute;
    /**
     * status:
     * B0-6 = Sun..Sat enable flag,B7 = Snooze enable flag
     */
    final byte status;

    public DailyAlarmModel(byte hour, byte minute, byte status) {
        this.hour = hour;
        this.minute = minute;
        this.status = status;
    }

    public byte getHour() {
        return hour;
    }

    public byte getMinute() {
        return minute;
    }

    public byte getStatus() {
        return status;
    }
}
