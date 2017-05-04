package com.dayton.drone.viewmodel;

import com.dayton.drone.view.SlideView;

import net.medcorp.library.worldclock.City;

/**
 * Created by karl-john on 5/8/2016.
 */

public class WorldClockViewModel {

    private final String cityName;

    private final int cityId;

    private SlideView slideView;

    private final int timeOffsetFromGmt;

    private boolean homeCity;

    public WorldClockViewModel(City city) {
        this.cityName = city.getName();
        timeOffsetFromGmt = city.getOffSetFromGMT();
        this.cityId = city.getId();
    }

    public boolean isHomeCity() {
        return homeCity;
    }

    public void setHomeCity(boolean homeCity) {
        this.homeCity = homeCity;
    }

    public String getName(){
        return cityName;
    }

    public int getOffSetFromGMT(){
            return timeOffsetFromGmt;
    }

    public int getCityId() {
        return cityId;
    }

    public SlideView getSlideView() {
        return slideView;
    }

    public void setSlideView(SlideView slideView) {
        this.slideView = slideView;
    }
}
