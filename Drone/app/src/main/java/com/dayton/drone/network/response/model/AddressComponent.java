package com.dayton.drone.network.response.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by med on 17/5/17.
 */

public class AddressComponent {
    @SerializedName("long_name")
    String longName;
    @SerializedName("short_name")
    String shortName;

    String[] types;

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public String[] getTypes() {
        return types;
    }
}
