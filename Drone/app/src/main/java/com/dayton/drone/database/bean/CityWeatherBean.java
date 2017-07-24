package com.dayton.drone.database.bean;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by med on 17/7/24.
 */

public class CityWeatherBean extends RealmObject {
    private String cityName;
    private RealmList<HourlyForecastBean> weatherData;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public RealmList<HourlyForecastBean> getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(RealmList<HourlyForecastBean> weatherData) {
        this.weatherData = weatherData;
    }
}
