package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/7/24.
 */

public class HourlyWeather {
    private String summary;
    private String icon;
    private HourlyData[] data;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public HourlyData[] getData() {
        return data;
    }

    public void setData(HourlyData[] data) {
        this.data = data;
    }
}
