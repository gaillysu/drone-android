package com.dayton.drone.event;

import com.dayton.drone.network.response.model.Location;

/**
 * Created by med on 17/6/19.
 */

public class PlaceChangedEvent {
    final Location location;
    final String address;
    final String name;

    public PlaceChangedEvent(Location location, String address, String name) {
        this.location = location;
        this.address = address;
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }
}
