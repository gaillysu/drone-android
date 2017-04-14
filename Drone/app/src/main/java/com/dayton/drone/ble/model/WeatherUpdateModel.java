package com.dayton.drone.ble.model;

/**
 * Created by med on 17/4/10.
 */

public class WeatherUpdateModel {
    public final static int MAXENTRY = 6;
    final byte id;
    final short temperature;
    final WeatherCode  status;

    public WeatherUpdateModel(byte id, short temperature, WeatherCode status) {
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

    public WeatherCode getStatus() {
        return status;
    }

    public enum WeatherCode {
        InvalidData(0x00),
        PartlyCloudyNight(0x01),
        PartlyCloudyDay(0x02),
        Tornado(0x03),
        Typhoon(0x04),
        Hurricane(0x05),
        Cloudy(0x06),
        Fog(0x07),
        Windy(0x08),
        Snow(0x09),
        RainLight(0x0A),
        RainHeavy(0x0B),
        Stormy(0x0C),
        ClearDay(0x0D),
        ClearNight(0x0E);
        final int id;
        WeatherCode(int id) {
            this.id = id;
        }
        public  int rawValue() {return id;}
    }
}
