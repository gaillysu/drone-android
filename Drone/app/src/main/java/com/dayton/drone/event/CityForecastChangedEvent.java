package com.dayton.drone.event;


/**
 * Created by med on 16/5/27.
 */
public class CityForecastChangedEvent {
    private final String name;
    private final float temp;
    private final String icon;
    private final int locationId;

    public CityForecastChangedEvent(String name, float temp, String icon,int locationId) {
        this.name = name;
        this.temp = temp;
        this.icon = icon;
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public float getTemp() {
        return temp;
    }

    public String getIcon() {
        return icon;
    }

    public int getLocationId() {
        return locationId;
    }
}
