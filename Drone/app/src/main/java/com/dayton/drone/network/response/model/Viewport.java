package com.dayton.drone.network.response.model;

import com.google.gson.annotations.SerializedName;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by med on 17/5/17.
 */

public class Viewport {
    @SerializedName("northeast")
    Location northEast;
    @SerializedName("southwest")
    Location southWest;
}
