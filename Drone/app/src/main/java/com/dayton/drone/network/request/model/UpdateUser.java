package com.dayton.drone.network.request.model;

/**
 * Created by med on 16/5/16.
 */
public class UpdateUser extends CreateUser {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
