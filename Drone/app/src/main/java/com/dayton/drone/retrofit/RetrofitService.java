package com.dayton.drone.retrofit;

import com.dayton.drone.retrofit.restapi.Drone;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import retrofit.RestAdapter;

/**
 * Created by med on 16/4/29.
 */
public class RetrofitService extends RetrofitGsonSpiceService {
    //TODO this url should be a public address
    private final static String BASE_URL = "https://drone.karljohnchow.com";

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(Drone.class);
    }

    @Override
    protected RestAdapter.Builder createRestAdapterBuilder() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.HEADERS)
                .setConverter(getConverter())
                .setEndpoint(getServerUrl());
        return builder;
    }
}
