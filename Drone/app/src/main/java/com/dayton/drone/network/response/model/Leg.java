package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/5/18.
 */

public class Leg {
    Distance distance;
    Duration duration;
    String end_address;
    Location end_location;
    String start_address;
    Location start_location;
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
