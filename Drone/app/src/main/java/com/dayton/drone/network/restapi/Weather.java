package com.dayton.drone.network.restapi;

import com.dayton.drone.network.response.model.GetWeatherModel;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by med on 16/4/29.
 */
public interface Weather {
    @GET("/data/2.5/weather")
    GetWeatherModel getWeather(@Query("q") String cityName,@Query("appid") String apiKey);
}
