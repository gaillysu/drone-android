package com.dayton.drone.network.response.model;

/**
 * Created by med on 16/5/3.
 */
public class StepsDetail {
    private int id;
    private int uid;
    private int steps;
    private StepsWithTimeZone date;

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

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public StepsWithTimeZone getDate() {
        return date;
    }

    public void setDate(StepsWithTimeZone date) {
        this.date = date;
    }
}
