package com.dayton.drone.network.request.model;



/**
 * Created by med on 16/5/3.
 */
public class CreateStepsObject {
    private String token;
    private CreateStepsParameters params;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CreateStepsParameters getParams() {
        return params;
    }

    public void setParams(CreateStepsParameters params) {
        this.params = params;
    }
}
