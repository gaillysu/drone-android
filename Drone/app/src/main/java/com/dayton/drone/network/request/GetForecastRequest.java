package com.dayton.drone.network.request;

import com.dayton.drone.network.response.model.GetForecastModel;
import com.dayton.drone.network.restapi.Weather;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by med on 17/4/14.
 */

public class GetForecastRequest extends RetrofitSpiceRequest<GetForecastModel,Weather> {
    private String cityName;
    private String apiKey;

    public GetForecastRequest(String cityName, String apiKey) {
        super(GetForecastModel.class, Weather.class);
        this.cityName = cityName;
        this.apiKey = apiKey;
    }

    @Override
    public GetForecastModel loadDataFromNetwork() throws Exception {
        return getService().getForecast(cityName,apiKey);
    }
}
