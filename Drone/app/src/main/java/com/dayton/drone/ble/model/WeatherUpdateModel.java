package com.dayton.drone.ble.model;

/**
 * Created by med on 17/4/10.
 */

public class WeatherUpdateModel {
    public final static int MAXENTRY = 6;
    final byte id;
    final short temperature;
    final byte  status;

    public WeatherUpdateModel(byte id, short temperature, byte status) {
        this.id = id;
        this.temperature = temperature;
        this.status = status;
    }

    public byte getId() {
        return id;
    }

    public short getTemperature() {
        return temperature;
    }

    public byte getStatus() {
        return status;
    }
}
