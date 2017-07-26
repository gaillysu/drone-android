package com.dayton.drone.network.response.model;

import java.util.Comparator;

/**
 * Created by med on 17/4/21.
 */

public class GetForecastModel {
    //pls refer to  https://darksky.net/dev/docs/forecast
    private float latitude;
    private float longitude;
    private String timezone;
    private int offset;
    private HourlyWeather hourly;

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public HourlyWeather getHourly() {
        return hourly;
    }

    public void setHourly(HourlyWeather hourly) {
        this.hourly = hourly;
    }
}
