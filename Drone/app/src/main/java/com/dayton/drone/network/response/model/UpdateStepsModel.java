package com.dayton.drone.network.response.model;

/**
 * Created by med on 16/5/3.
 */
public class UpdateStepsModel {
    private StepsDetail steps;
    private float version;
    private String message;
    private int status;

    public StepsDetail getSteps() {
        return steps;
    }

    public void setSteps(StepsDetail steps) {
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
