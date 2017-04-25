package com.dayton.drone.ble.util;

/**
 * Created by med on 17/4/24.
 */

public enum WeatherCode {
    INVALID_DATA(0X00),
    PARTLY_CLOUDY_NIGHT(0X01),
    PARTLY_CLOUDY_DAY(0X02),
    TORNADO(0X03),
    TYPHOON(0X04),
    HURRICANE(0X05),
    CLOUDY(0X06),
    FOG(0X07),
    WINDY(0X08),
    SNOW(0X09),
    RAIN_LIGHT(0X0A),
    RAIN_HEAVY(0X0B),
    STORMY(0X0C),
    CLEAR_DAY(0X0D),
    CLEAR_NIGHT(0X0E);

    final int id;

    WeatherCode(int id) {
        this.id = id;
    }

    public  int rawValue() {return id;}
}
