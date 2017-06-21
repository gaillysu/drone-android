package com.dayton.drone.network.request;

import com.dayton.drone.network.response.model.GetGeocodeModel;
import com.dayton.drone.network.restapi.GoogleMap;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by med on 17/4/14.
 */

public class GetGeocodeRequest extends RetrofitSpiceRequest<GetGeocodeModel,GoogleMap> {
    private String address;
    private String apiKey;
    private String language;

    public GetGeocodeRequest(String address, String apiKey,String language) {
        super(GetGeocodeModel.class, GoogleMap.class);
        this.address = address;
        this.apiKey = apiKey;
        this.language = language;
    }

    @Override
    public GetGeocodeModel loadDataFromNetwork() throws Exception {
        return getService().getGeocode(address,apiKey, language);
    }
}
