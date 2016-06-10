package com.dayton.drone.network.request.model;

/**
 * Created by Administrator on 2016/6/8.
 */
public class RequestChangePasswordBodyParameters {
    private String token;
    private ChangePasswordModel changePasswordModel;

    public RequestChangePasswordBodyParameters(String token, ChangePasswordModel changePasswordModel) {
        this.token = token;
        this.changePasswordModel = changePasswordModel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
