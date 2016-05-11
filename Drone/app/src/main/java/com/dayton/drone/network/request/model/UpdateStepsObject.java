package com.dayton.drone.network.request.model;



/**
 * Created by med on 16/5/3.
 */
public class UpdateStepsObject {
    private String token;
    private UpdateStepsParameters params;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UpdateStepsParameters getParams() {
        return params;
    }

    public void setParams(UpdateStepsParameters params) {
        this.params = params;
    }
}
