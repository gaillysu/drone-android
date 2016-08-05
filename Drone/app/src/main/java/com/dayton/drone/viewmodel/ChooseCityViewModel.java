package com.dayton.drone.viewmodel;

/**
 * Created by karl-john on 5/8/2016.
 */

public class ChooseCityViewModel {

    private String displayName;

    private int cityId;


    public ChooseCityViewModel(String displayName, int worldClockId) {
        this.displayName = displayName;
        this.cityId = worldClockId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
