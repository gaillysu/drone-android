package com.dayton.drone.network;

import com.dayton.drone.network.restapi.Weather;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import retrofit.RestAdapter;

/**
 * Created by med on 16/4/29.
 */
public class RetrofitWeatherService extends RetrofitGsonSpiceService {

    private final static String BASE_URL = "https://api.darksky.net";

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(Weather.class);
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        return new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setConverter(getConverter())
                .setEndpoint(getServerUrl());
    }
}
