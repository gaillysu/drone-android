package com.dayton.drone.network.response.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by med on 17/5/18.
 */

public class Bound {
    @SerializedName("northeast")
    Location northEast;
    @SerializedName("southwest")
    Location southWest;
}
