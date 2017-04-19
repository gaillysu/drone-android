package com.dayton.drone.event;


import com.dayton.drone.network.response.model.GetWeatherModel;

/**
 * Created by med on 16/5/27.
 */
public class CityWeatherChangedEvent {
    private final String name;
    private final GetWeatherModel weatherModel;

    public CityWeatherChangedEvent(String name, GetWeatherModel weatherModel) {
        this.name = name;
        this.weatherModel = weatherModel;
    }

    public GetWeatherModel getWeatherModel() {
        return weatherModel;
    }

    public String getName() {
        return name;
    }
}
