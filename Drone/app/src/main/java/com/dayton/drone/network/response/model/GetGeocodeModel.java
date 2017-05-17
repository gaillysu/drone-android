package com.dayton.drone.network.response.model;

/**
 * Created by med on 17/5/17.
 */

public class GetGeocodeModel {
//pls refer to https://developers.google.com/maps/documentation/geocoding/intro#GeocodingResponses
    GeocodeResult[] results;
    String status;

    public GeocodeResult[] getResults() {
        return results;
    }
}
