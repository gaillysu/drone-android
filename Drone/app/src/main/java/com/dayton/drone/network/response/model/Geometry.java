package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/5/17.
 */

public class Geometry {
    Location location;
    String location_type;
    Viewport viewport;

    public Location getLocation() {
        return location;
    }
}
