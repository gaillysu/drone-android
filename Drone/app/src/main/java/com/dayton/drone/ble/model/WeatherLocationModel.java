package com.dayton.drone.ble.model;

/**
 * Created by med on 17/4/10.
 */

public class WeatherLocationModel {
    public final static int MAXENTRY = 6;
    final byte id;
    final byte length;
    final String title;

    public WeatherLocationModel(byte id, byte length, String title) {
        this.id = id;
        this.length = length;
        this.title = title;
    }

    public byte getId() {
        return id;
    }

    public byte getLength() {
        return length;
    }

    public String getTitle() {
        return title;
    }
}
