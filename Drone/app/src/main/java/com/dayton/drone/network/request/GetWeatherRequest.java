package com.dayton.drone.network.request;

import com.dayton.drone.network.response.model.GetWeatherModel;
import com.dayton.drone.network.restapi.Weather;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by med on 17/4/14.
 */

public class GetWeatherRequest extends RetrofitSpiceRequest<GetWeatherModel,Weather> {
    private String cityName;
    private String apiKey;

    public GetWeatherRequest(String cityName,String apiKey) {
        super(GetWeatherModel.class, Weather.class);
        this.cityName = cityName;
        this.apiKey = apiKey;
    }

    @Override
    public GetWeatherModel loadDataFromNetwork() throws Exception {
        return getService().getWeather(cityName,apiKey);
    }
}
