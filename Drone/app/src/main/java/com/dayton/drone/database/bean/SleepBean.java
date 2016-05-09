package com.dayton.drone.database.bean;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by gaillysu on 15/11/17.
 */
public class SleepBean {

    /**
     * field name and initialize value, Primary field
     */
    public static final String fID = "id";
    @DatabaseField(id = true)
    private int id = (int) Math.floor(Math.random()*Integer.MAX_VALUE);

    /**
     * which user ID
     */
    public static final String fUserID = "userID";
    @DatabaseField
    private String userID;

    /**
     * unix timestamp of the start of the accumulation, created date ?
     */
    public static final String fTimeframe = "timeFrame";
    @DatabaseField
    private long timeFrame;

    /**
     *  date, one day which is Year/Month/Day
     */
    public static final String fDate = "date";
    @DatabaseField
    private long date;

    public static final String fWakeupTime = "wakeupTime";
    @DatabaseField
    private int wakeupTime;

    public static final String fLightSleepTime = "lightSleepTime";
    @DatabaseField
    private int lightSleepTime;

    public static final String fDeepSleepTime = "deepSleepTime";
    @DatabaseField
    private int deepSleepTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(long timeFrame) {
        this.timeFrame = timeFrame;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getWakeupTime() {
        return wakeupTime;
    }

    public void setWakeupTime(int wakeupTime) {
        this.wakeupTime = wakeupTime;
    }

    public int getLightSleepTime() {
        return lightSleepTime;
    }

    public void setLightSleepTime(int lightSleepTime) {
        this.lightSleepTime = lightSleepTime;
    }

    public int getDeepSleepTime() {
        return deepSleepTime;
    }

    public void setDeepSleepTime(int deepSleepTime) {
        this.deepSleepTime = deepSleepTime;
    }
}
