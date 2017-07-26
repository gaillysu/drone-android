package com.dayton.drone.network.request;

import com.dayton.drone.network.response.model.GetForecastModel;
import com.dayton.drone.network.restapi.Weather;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import java.util.Locale;

/**
 * Created by med on 17/4/14.
 */

public class GetForecastRequest extends RetrofitSpiceRequest<GetForecastModel,Weather> {
    private String apiKey;
    private String location;

    public GetForecastRequest(String location, String apiKey) {
        super(GetForecastModel.class, Weather.class);
        this.location = location;
        this.apiKey = apiKey;
    }

    @Override
    public GetForecastModel loadDataFromNetwork() throws Exception {
        return getService().getForecast(apiKey,location, Locale.getDefault().getLanguage(),"currently,daily,flags","ca");
    }
}
