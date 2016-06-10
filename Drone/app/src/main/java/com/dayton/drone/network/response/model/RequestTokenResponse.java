package com.dayton.drone.network.response.model;

import com.dayton.drone.network.response.BaseResponse;

/**
 * Created by Administrator on 2016/6/8.
 */
public class RequestTokenResponse extends BaseResponse
{
   private UserWithPasswordToken user;

    public UserWithPasswordToken getUser() {
        return user;
    }

    public void setUser(UserWithPasswordToken user) {
        this.user = user;
    }
}
