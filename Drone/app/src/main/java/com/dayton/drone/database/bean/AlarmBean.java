package com.dayton.drone.database.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by med on 17/6/27.
 */

public class AlarmBean extends RealmObject {
    @PrimaryKey
    private int id =  (int) (Math.floor(Math.random() * Integer.MAX_VALUE));

    private byte hour;
    private byte minute;
    /**
     * status:
     * B0-6 = Sun..Sat enable flag,B7 = Snooze enable flag
     */
    private byte status;
    private String label;
    private boolean enable;


    public byte getHour() {
        return hour;
    }

    public void setHour(byte hour) {
        this.hour = hour;
    }

    public byte getMinute() {
        return minute;
    }

    public void setMinute(byte minute) {
        this.minute = minute;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
