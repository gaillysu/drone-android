package com.dayton.drone.retrofit.request.user;

/**
 * Created by med on 16/5/3.
 */
public class LoginUserObject {
    private String token;
    private LoginParameters params;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginParameters getParams() {
        return params;
    }

    public void setParams(LoginParameters params) {
        this.params = params;
    }
}
