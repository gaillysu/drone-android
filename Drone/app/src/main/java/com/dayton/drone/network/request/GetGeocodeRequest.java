package com.dayton.drone.network.request;

import com.dayton.drone.network.response.model.GetForecastModel;
import com.dayton.drone.network.response.model.GetGeocodeModel;
import com.dayton.drone.network.restapi.GoogleMap;
import com.dayton.drone.network.restapi.Weather;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by med on 17/4/14.
 */

public class GetGeocodeRequest extends RetrofitSpiceRequest<GetGeocodeModel,GoogleMap> {
    private String address;
    private String apiKey;

    public GetGeocodeRequest(String address, String apiKey) {
        super(GetGeocodeModel.class, GoogleMap.class);
        this.address = address;
        this.apiKey = apiKey;
    }

    @Override
    public GetGeocodeModel loadDataFromNetwork() throws Exception {
        return getService().getGeocode(address,apiKey);
    }
}
