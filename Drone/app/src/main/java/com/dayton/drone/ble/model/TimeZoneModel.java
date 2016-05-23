package com.dayton.drone.ble.model;

/**
 * Created by med on 16/4/22.
 */
public class TimeZoneModel {
    private float timeZone; //for example: Bei jing Zone: +8 ,New York: -5, Central Australia: +9.5
    private String timeZoneName;
    public TimeZoneModel(float timeZone,String timeZoneName)
    {
        this.timeZone = timeZone;
        this.timeZoneName = timeZoneName;
    }
    public float getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(float timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }
    /**
    return how many bytes in the response packet for this zone
     */
    public int getTimeZoneBytesCount()
    {
        return 2+timeZoneName.length();
    }
}
