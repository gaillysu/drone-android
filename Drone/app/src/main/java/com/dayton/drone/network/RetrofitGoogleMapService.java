package com.dayton.drone.network;

import com.dayton.drone.network.restapi.GoogleMap;
import com.dayton.drone.network.restapi.Weather;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import retrofit.RestAdapter;

/**
 * Created by med on 17/5/17.
 */
public class RetrofitGoogleMapService extends RetrofitGsonSpiceService {

    private final static String BASE_URL = "https://maps.googleapis.com/maps/api";

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(GoogleMap.class);
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        return new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setConverter(getConverter())
                .setEndpoint(getServerUrl());
    }
}
