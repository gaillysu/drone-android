package com.dayton.drone.network.request.model;

/**
 * Created by Administrator on 2016/6/15.
 */
public class CheckEmailUserParameters {

      private CheckEmailUserBody user;

    public CheckEmailUserParameters(CheckEmailUserBody user){
        this.user = user;
    }

    public CheckEmailUserBody getUser() {
        return user;
    }

    public void setUser(CheckEmailUserBody user) {
        this.user = user;
    }
}
