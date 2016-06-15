package com.dayton.drone.network.request.model;

import com.dayton.drone.network.response.BaseResponse;

/**
 * Created by Administrator on 2016/6/15.
 */
public class CheckEmailResponse extends BaseResponse{
    private UserWithCheckEmail user;

    public UserWithCheckEmail getUser() {
        return user;
    }

    public void setUser(UserWithCheckEmail user) {
        this.user = user;
    }
}
