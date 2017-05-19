package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/5/18.
 */

public class Step {
    Distance distance;
    Duration duration;
    Location end_location;
    String html_instructions;
    String maneuver;
    Polyline polyline;
    Location start_location;
    String travel_mode;

    public Location getEnd_location() {
        return end_location;
    }

    public Location getStart_location() {
        return start_location;
    }
}
