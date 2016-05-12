package com.dayton.drone.modle;

import com.dayton.drone.database.bean.WorldClockBean;

/**
 * Created by med on 16/5/9.
 */
public class WorldClock extends WorldClockBean {
    private String city;
    private String cityLocation;
    private String modernDate;
    private String timeDifference;
    private String cityCurrentTime ;

    public String getCity() {

        return city;
    }

    public void setCityLocation(String cityLocation) {
        this.cityLocation = cityLocation;
    }
    public String getCityLocation() {

        return cityLocation;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getModernDate() {
        return modernDate;
    }

    public void setModernDate(String modernDate) {
        this.modernDate = modernDate;
    }

    public String getTimeDifference() {
        return timeDifference;
    }

    public void setTimeDifference(String timeDifference) {
        this.timeDifference = timeDifference;
    }

    public String getCityCurrentTime() {
        return cityCurrentTime;
    }

    public void setCityCurrentTime(String cityCurrentTime) {
        this.cityCurrentTime = cityCurrentTime;
    }
}
