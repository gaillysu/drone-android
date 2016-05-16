package com.dayton.drone.network.response.model;

/**
 * Created by med on 16/5/16.
 */
public class UserWithPassword extends UserWithID {
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
