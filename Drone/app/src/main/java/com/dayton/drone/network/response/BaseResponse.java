package com.dayton.drone.network.response;

/**
 * Created by med on 16/5/10.
 */
public abstract class BaseResponse {
    private float version;
    private String message;
    private int status;

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