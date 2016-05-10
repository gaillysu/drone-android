package com.dayton.drone.bean;

/**
 * Created by Administrator on 2016/5/10.
 */
public class WorldClockListBean {
    private String city;
    private String modernDate;
    private String timeDifference;
    private String cityCurrentTime ;

    public String getCity() {

        return city;
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
