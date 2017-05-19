package com.dayton.drone.network.request;

import com.dayton.drone.network.response.model.GetGeocodeModel;
import com.dayton.drone.network.response.model.GetRouteMapModel;
import com.dayton.drone.network.restapi.GoogleMap;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by med on 17/4/14.
 */

public class GetRouteMapRequest extends RetrofitSpiceRequest<GetRouteMapModel,GoogleMap> {
    private String originAddress;
    private String destinationAddress;
    private String mode;
    private String apiKey;

    public GetRouteMapRequest(String originAddress, String destinationAddress,String mode,String apiKey) {
        super(GetRouteMapModel.class, GoogleMap.class);
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.mode = mode;
        this.apiKey = apiKey;
    }

    public GetRouteMapRequest(double originLatitude, double originLongitude,double destinationLatitude, double destinationLongitude,String mode,String apiKey) {
        super(GetRouteMapModel.class, GoogleMap.class);
        this.originAddress = new String()+ originLatitude + "," + originLongitude;
        this.destinationAddress = new String()+ destinationLatitude + "," + destinationLongitude;
        this.mode = mode;
        this.apiKey = apiKey;
    }

    @Override
    public GetRouteMapModel loadDataFromNetwork() throws Exception {
        return getService().getRouteMap(originAddress,destinationAddress,mode,apiKey);
    }
}
