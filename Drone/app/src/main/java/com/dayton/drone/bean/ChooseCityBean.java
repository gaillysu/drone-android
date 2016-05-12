package com.dayton.drone.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ChooseCityBean implements Serializable {

    private String cityLocation;
    private String cityName;

    @Override
    public String toString() {
        return "ChooseCityBean{" +
                "cityLocation='" + cityLocation + '\'' +
                ", cityName='" + cityName + '\'' +
                '}';
    }

    public String getCityLocation() {
        return cityLocation;
    }

    public void setCityLocation(String cityLocation) {
        this.cityLocation = cityLocation;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }



}
