package com.dayton.drone.retrofit.request.user;

import com.dayton.drone.retrofit.model.User;

/**
 * Created by med on 16/5/3.
 */
public class CreateParameters {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
