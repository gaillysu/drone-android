package com.dayton.drone.retrofit.model;

/**
 * Created by med on 16/5/3.
 */
public class StepsDetailModel {
    private int id;
    private int uid;
    private int steps;
    private StepsDateModel date;

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

    public StepsDateModel getDate() {
        return date;
    }

    public void setDate(StepsDateModel date) {
        this.date = date;
    }
}
