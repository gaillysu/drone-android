package com.dayton.drone.retrofit.request.steps;

import com.dayton.drone.retrofit.model.StepsModel;

/**
 * Created by med on 16/5/3.
 */
public class CreateStepsModel {
    private StepsModel[] steps;
    private float version;
    private String message;
    private int status;

    public StepsModel[] getSteps() {
        return steps;
    }

    public void setSteps(StepsModel[] steps) {
        this.steps = steps;
    }

    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
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