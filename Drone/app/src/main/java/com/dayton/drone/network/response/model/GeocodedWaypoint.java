package com.dayton.drone.network.response.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by med on 17/5/17.
 */

public class GeocodedWaypoint {
    @SerializedName("geocoder_status")
    String geocoderStatus;
    @SerializedName("place_id")
    String placeId;
    String[] types;
}
