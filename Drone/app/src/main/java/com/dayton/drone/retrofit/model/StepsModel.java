package com.dayton.drone.retrofit.model;

/**
 * Created by med on 16/5/3.
 */
public class StepsModel {
    StepsDetailModel steps;
    private String message;
    private int status;

    public StepsDetailModel getSteps() {
        return steps;
    }

    public void setSteps(StepsDetailModel steps) {
        this.steps = steps;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
