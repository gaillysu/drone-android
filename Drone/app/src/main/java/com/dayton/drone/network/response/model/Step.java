package com.dayton.drone.network.response.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by med on 17/5/18.
 */

public class Step {
    Distance distance;
    Duration duration;
    @SerializedName("end_location")
    Location endLocation;
    @SerializedName("html_instructions")
    String htmlInstructions;
    String maneuver;
    Polyline polyline;
    @SerializedName("start_location")
    Location startLocation;
    @SerializedName("travel_mode")
    String travelMode;

    public Location getEndLocation() {
        return endLocation;
    }

    public Location getStartLocation() {
        return startLocation;
    }

    public Polyline getPolyline() {
        return polyline;
    }
}
