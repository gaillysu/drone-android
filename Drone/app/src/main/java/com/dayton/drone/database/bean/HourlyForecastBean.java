package com.dayton.drone.database.bean;

import io.realm.RealmObject;

/**
 * Created by med on 17/7/24.
 */

public class HourlyForecastBean extends RealmObject {
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

    public void setTime(long time) {
        this.time = time;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }
}
