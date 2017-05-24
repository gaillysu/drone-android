package com.dayton.drone.network.restapi;

import com.dayton.drone.network.response.model.GetGeocodeModel;
import com.dayton.drone.network.response.model.GetRouteMapModel;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by med on 17/5/17.
 */
public interface GoogleMap {

    @GET("/geocode/json")
    GetGeocodeModel getGeocode(@Query("address") String name, @Query("key") String apiKey);

    @GET("/directions/json")
    GetRouteMapModel getRouteMap(@Query("origin") String originAddress, @Query("destination") String destinationAddress,@Query("mode") String mode, @Query("key") String apiKey);
}
