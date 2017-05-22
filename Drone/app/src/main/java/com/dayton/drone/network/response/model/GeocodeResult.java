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

    // transient fields only is used for UI
    transient String formattedCityRegion;
    transient String formattedRoad;
    transient String formattedDistance="";

    public String getFormatted_address() {
        return formatted_address;
    }

    public Address_Component[] getAddress_components() {
        return address_components;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getFormattedCityRegion() {
        return formattedCityRegion;
    }

    public void setFormattedCityRegion(String formattedCityRegion) {
        this.formattedCityRegion = formattedCityRegion;
    }

    public String getFormattedRoad() {
        return formattedRoad;
    }

    public void setFormattedRoad(String formattedRoad) {
        this.formattedRoad = formattedRoad;
    }

    public String getFormattedDistance() {
        return formattedDistance;
    }

    public void setFormattedDistance(String formattedDistance) {
        this.formattedDistance = formattedDistance;
    }
}
