package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/7/24.
 */

public class HourlyData {
    private long time;
    private String summary;
    private String icon;
    private float temperature;

    public long getTime() {
        return time;
    }

    public String getSummary() {
        return summary;
    }

    public String getIcon() {
        return icon;
    }

    public float getTemperature() {
        return temperature;
    }
}
