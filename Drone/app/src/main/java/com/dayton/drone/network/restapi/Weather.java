package com.dayton.drone.network.restapi;

import com.dayton.drone.network.response.model.GetForecastModel;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by med on 16/4/29.
 */
public interface Weather {

    @GET("/data/2.5/forecast")
    GetForecastModel getForecast(@Query("q") String cityName, @Query("appid") String apiKey);
}
