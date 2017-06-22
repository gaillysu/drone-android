package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/5/17.
 */

public class Location {
    double lat;
    double lng;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
