package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/5/17.
 */

public class GetRouteMapModel {
    Geocoded_waypoint[] geocoded_waypoints;
    Route[] routes;
    String status;

    public Route[] getRoutes() {
        return routes;
    }
}
