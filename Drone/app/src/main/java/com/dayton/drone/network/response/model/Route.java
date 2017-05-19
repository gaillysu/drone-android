package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/5/17.
 */

public class Route {
    Bound bounds;
    String copyrights;
    Leg[] legs;
    Overview_polyline overview_polyline;
    String summary;
    String[] warnings;
    int[] waypoint_order;

    public Leg[] getLegs() {
        return legs;
    }
}
