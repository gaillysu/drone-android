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
     *  date, one day which is Year/Month/Day
     */
    public static final String fDate = "date";
    @DatabaseField
    private long date;


    /**
     * this is accumulation steps for every hour a day, like this [100,200,300,....,2400]
     */
    public static final String fHourlySteps = "hourlySteps";
    @DatabaseField
    private String hourlySteps = "[0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]";

    /**
     * one day's total distance ,unit is meter.
     */
    public static final String fDistance = "distance";
    @DatabaseField
    private int distance;


    /**
     * the cloudID is the unique value that saved on Cloud server, "-1" menas that no sync with cloud server
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

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getHourlySteps() {
        return hourlySteps;
    }

    public void setHourlySteps(String hourlySteps) {
        this.hourlySteps = hourlySteps;
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
