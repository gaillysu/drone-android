package com.dayton.drone.network.request.model;

/**
 * Created by med on 16/5/16.
 */
public class UpdateUserObject {
    private String token;
    private UpdateUserParameters params;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UpdateUserParameters getParams() {
        return params;
    }

    public void setParams(UpdateUserParameters params) {
        this.params = params;
    }
}
