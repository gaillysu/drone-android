package com.dayton.drone.network.response.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by med on 17/5/17.
 */
public class GeocodeResult {
    @SerializedName("address_components")
    AddressComponent[] addressComponents;
    @SerializedName("formatted_address")
    String formattedAddress;
    Geometry geometry;
    @SerializedName("place_id")
    String placeId;
    String[] types;

    // transient fields only is used for UI
    @Expose(serialize=false,deserialize=false)
    String formattedCityRegion;
    @Expose(serialize=false,deserialize=false)
    String formattedRoad;
    @Expose(serialize=false,deserialize=false)
    String formattedDistance="";

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public AddressComponent[] getAddressComponents() {
        return addressComponents;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
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
