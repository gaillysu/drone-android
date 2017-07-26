package com.dayton.drone.network.restapi;

import com.dayton.drone.network.response.model.GetForecastModel;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by med on 16/4/29.
 */
public interface Weather {

    @GET("/forecast/{apiKey}/{location}")
    GetForecastModel getForecast(@Path("apiKey") String apiKey, @Path("location") String location, @Query("lang") String language,@Query("exclude") String exclude,@Query("units") String units);

}
