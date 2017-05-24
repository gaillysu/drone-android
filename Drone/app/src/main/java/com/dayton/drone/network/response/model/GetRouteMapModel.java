package com.dayton.drone.network.response.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by med on 17/5/17.
 */

public class GetRouteMapModel {
    @SerializedName("geocoded_waypoints")
    GeocodedWaypoint[] geocodedWaypoints;
    Route[] routes;
    String status;

    public Route[] getRoutes() {
        return routes;
    }
}
