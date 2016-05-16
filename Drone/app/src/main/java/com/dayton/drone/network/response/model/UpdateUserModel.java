package com.dayton.drone.network.response.model;

import com.dayton.drone.network.response.BaseResponse;

/**
 * Created by med on 16/5/16.
 */
public class UpdateUserModel extends BaseResponse {
    private UserWithPassword user;

    public UserWithPassword getUser() {
        return user;
    }

    public void setUser(UserWithPassword user) {
        this.user = user;
    }
}
