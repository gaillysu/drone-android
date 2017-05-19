package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/5/17.
 */

public class Address_Component {
    String long_name;
    String short_name;
    String[] types;

    public String getLong_name() {
        return long_name;
    }

    public String getShort_name() {
        return short_name;
    }

    public String[] getTypes() {
        return types;
    }
}
