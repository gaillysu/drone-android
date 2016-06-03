package com.dayton.drone.network.response.model;

import com.dayton.drone.network.response.BaseResponse;

/**
 * Created by med on 16/5/16.
 */
public class UpdateUserModel extends BaseResponse {
    private UserWithLocation user;

    public UserWithLocation getUser() {
        return user;
    }

    public void setUser(UserWithLocation user) {
        this.user = user;
    }
}
