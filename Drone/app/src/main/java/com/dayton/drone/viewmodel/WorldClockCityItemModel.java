package com.dayton.drone.viewmodel;

import net.medcorp.library.worldclock.City;

/**
 * Created by karl-john on 5/8/2016.
 */

public class WorldClockCityItemModel {

    private String cityName;

    private int cityId;

    private int timeOffsetFromGmt;

    public WorldClockCityItemModel(City city) {
        this.cityName = city.getName();
        timeOffsetFromGmt = city.getOffSetFromGMT();
        this.cityId = city.getId();
    }

    public int getOffSetFromGMT() {
        return timeOffsetFromGmt;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }
}
