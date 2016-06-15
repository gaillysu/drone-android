package com.dayton.drone.network.request.model;

/**
 * Created by Administrator on 2016/6/15.
 */
public class CheckUserEmailObject {

    private String token;
    private CheckEmailUserParameters params;

    public CheckUserEmailObject(CheckEmailUserParameters object, String token) {
        this.token = token;
        this.params = object;
    }

    public CheckUserEmailObject() {

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CheckEmailUserParameters getParams() {
        return params;
    }

    public void setParams(CheckEmailUserParameters params) {
        this.params = params;
    }
}
