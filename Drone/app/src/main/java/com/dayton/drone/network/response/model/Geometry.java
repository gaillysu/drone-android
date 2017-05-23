package com.dayton.drone.network.response.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by med on 17/5/17.
 */

public class Geometry {
    Location location;
    String location_type;
    @JsonProperty
    Viewport viewport;

    public Location getLocation() {
        return location;
    }
}
