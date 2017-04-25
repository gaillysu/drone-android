package com.dayton.drone.ble.model;

import com.dayton.drone.ble.util.WeatherCode;

/**
 * Created by med on 17/4/10.
 */

public class WeatherUpdateModel {
    public final static int MAXENTRY = 6;
    final byte id;
    final short temperature;
    final WeatherCode status;

    public WeatherUpdateModel(int id, float temperature, WeatherCode status) {
        this.id = (byte)id;
        this.temperature = (short) temperature;
        this.status = status;
    }

    public byte getId() {
        return id;
    }

    public short getTemperature() {
        return temperature;
    }

    public WeatherCode getStatus() {
        return status;
    }


}
