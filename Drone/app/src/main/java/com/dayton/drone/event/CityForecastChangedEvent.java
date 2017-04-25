package com.dayton.drone.event;


/**
 * Created by med on 16/5/27.
 */
public class CityForecastChangedEvent {
    private final String name;
    private final float temp;
    private final int weatherId;
    private final String main;

    public CityForecastChangedEvent(String name, float temp, int weatherId,String main) {
        this.name = name;
        this.temp = temp;
        this.weatherId = weatherId;
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public float getTemp() {
        return temp;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public String getMain() {
        return main;
    }
}
