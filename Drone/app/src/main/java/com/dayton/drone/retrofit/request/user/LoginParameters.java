package com.dayton.drone.retrofit.request.user;

import com.dayton.drone.retrofit.model.User;
import com.dayton.drone.retrofit.model.UserLogin;

/**
 * Created by med on 16/5/3.
 */
public class LoginParameters {
    private UserLogin user;

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }
}
