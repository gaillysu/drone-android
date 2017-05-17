package com.dayton.drone.network;

import android.content.Context;

import com.dayton.drone.R;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by med on 16/4/29.
 */
public class RetrofitManager {
    private Context context;
    private SpiceManager spiceManager;
    private SpiceManager spiceWeatherManager;
    private SpiceManager spiceGoogleMapManager;
    public  RetrofitManager(Context context){
        this.context = context;
        spiceManager = new SpiceManager(RetrofitService.class);
        spiceWeatherManager = new SpiceManager(RetrofitWeatherService.class);
        spiceGoogleMapManager = new SpiceManager(RetrofitGoogleMapService.class);
        startSpiceManager();
    }

    private void startSpiceManager()
    {
        if(!spiceManager.isStarted()) {
            spiceManager.start(context);
        }
        if(!spiceWeatherManager.isStarted()) {
            spiceWeatherManager.start(context);
        }
        if(!spiceGoogleMapManager.isStarted()) {
            spiceGoogleMapManager.start(context);
        }
    }
    public void stopSpiceManager()
    {
        if(spiceManager!=null) {
            spiceManager.shouldStop();
        }
        if(spiceWeatherManager!=null) {
            spiceWeatherManager.shouldStop();
        }
        if(spiceGoogleMapManager!=null) {
            spiceGoogleMapManager.shouldStop();
        }
    }

    public void execute(SpiceRequest request, RequestListener listener){
        spiceManager.execute(request, listener);
    }
    public void requestWeather(SpiceRequest request, RequestListener listener){
        spiceWeatherManager.execute(request, listener);
    }
    public void executeGoogleMapApi(SpiceRequest request, RequestListener listener){
        spiceGoogleMapManager.execute(request, listener);
    }
    public String getGoogleMapApiKey(){
        return context.getString(R.string.google_geo_key);
    }

    public String getWeatherApiKey(){
        return context.getString(R.string.weather_api_key);
    }

    public String getAccessToken(){
        return context.getString(R.string.token);
    }
}
