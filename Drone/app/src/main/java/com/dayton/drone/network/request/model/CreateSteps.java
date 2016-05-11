package com.dayton.drone.network.request.model;

/**
 * Created by med on 16/5/3.
 */
public class CreateSteps {
    private int uid;
    private int steps;
    private String date;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
