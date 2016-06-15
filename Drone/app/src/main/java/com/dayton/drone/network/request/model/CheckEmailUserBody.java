package com.dayton.drone.network.request.model;

/**
 * Created by Administrator on 2016/6/15.
 */
public class CheckEmailUserBody {
    private String email;

    public CheckEmailUserBody(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
