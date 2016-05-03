package com.dayton.drone.retrofit.request.user;

import com.dayton.drone.retrofit.model.UserModel;

/**
 * Created by med on 16/5/3.
 */
public class CreateUserModel {
    private UserModel user;
    private float version;
    private String message;
    private int status;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
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
