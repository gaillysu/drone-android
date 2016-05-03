package com.dayton.drone.retrofit.request.user;

/**
 * Created by med on 16/5/3.
 */
public class CreateUserObject {
    private String token;
    private CreateParameters params;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CreateParameters getParams() {
        return params;
    }

    public void setParams(CreateParameters params) {
        this.params = params;
    }
}
