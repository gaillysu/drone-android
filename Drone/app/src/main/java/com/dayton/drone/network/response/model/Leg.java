package com.dayton.drone.network.response.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by med on 17/5/18.
 */

public class Leg {
    Distance distance;
    Duration duration;
    @SerializedName("end_address")
    String endAddress;
    @SerializedName("end_location")
    Location endLocation;
    @SerializedName("start_address")
    String startAddress;
    @SerializedName("start_location")
    Location startLocation;
    Step[] steps;

    public Distance getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public Step[] getSteps() {
        return steps;
    }
}
