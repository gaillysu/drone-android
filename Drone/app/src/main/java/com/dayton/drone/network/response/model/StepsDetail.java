package com.dayton.drone.network.response.model;

/**
 * Created by med on 16/5/3.
 */
public class StepsDetail {
    private int id;
    private int uid;
    private String steps;
    private DateWithTimeZone date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public DateWithTimeZone getDate() {
        return date;
    }

    public void setDate(DateWithTimeZone date) {
        this.date = date;
    }

}
