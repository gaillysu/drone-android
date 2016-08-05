package com.dayton.drone.viewmodel;

import com.dayton.drone.view.SlideView;

/**
 * Created by karl-john on 5/8/2016.
 */

public class WorldClockViewModel {

    private String cityName;

    private int time;

    private int cityId;

    private SlideView slideView;

    public WorldClockViewModel(String cityName, int time, int cityId) {
        this.cityName = cityName;
        this.time = time;
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public SlideView getSlideView() {
        return slideView;
    }

    public void setSlideView(SlideView slideView) {
        this.slideView = slideView;
    }
}
