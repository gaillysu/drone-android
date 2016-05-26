package com.dayton.drone.database.bean;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by gaillysu on 15/11/17.
 */
public class StepsBean {
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
     * unix timestamp of the start of the accumulation, created date and time
     */
    public static final String fTimeframe = "timeFrame";
    @DatabaseField
    private long timeFrame = new Date().getTime();

    /**
     *  date, one day which is Year/Month/Day
     */
    public static final String fDate = "date";
    @DatabaseField
    private long date;


    /**
     * time frame total steps
     */
    public static final String fSteps = "steps";
    @DatabaseField
    private int steps;

    /**
     * one day's total distance ,unit is meter.
     */
    public static final String fDistance = "distance";
    @DatabaseField
    private int distance;


    /**
     * the cloudID is the unique value that saved on Cloud server, -1 menas that no sync with cloud server
     */
    public static final String fCloudID = "cloudID";
    @DatabaseField
    private int cloudID = -1;


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

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getCloudID() {
        return cloudID;
    }

    public void setCloudID(int cloudID) {
        this.cloudID = cloudID;
    }
}
