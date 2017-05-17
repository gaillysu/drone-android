package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/5/17.
 */

public class GeocodeResult {
    Address_Component[] address_components;
    String formatted_address;
    Geometry geometry;
    String place_id;
    String[] types;

    public String getFormatted_address() {
        return formatted_address;
    }

    public Geometry getGeometry() {
        return geometry;
    }
}
