package com.dayton.drone.network.response.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by med on 17/5/17.
 */

public class Route {
    Bound bounds;
    String copyrights;
    Leg[] legs;
    @SerializedName("overview_polyline")
    OverviewPolyline overviewPolyline;
    String summary;
    String[] warnings;
    @SerializedName("waypoint_order")
    int[] waypointOrder;

    public Leg[] getLegs() {
        return legs;
    }
}
