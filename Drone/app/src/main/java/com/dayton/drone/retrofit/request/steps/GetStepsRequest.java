package com.dayton.drone.retrofit.request.steps;

import com.dayton.drone.retrofit.request.BaseRetroRequest;
import com.dayton.drone.retrofit.restapi.Drone;

/**
 * Created by med on 16/5/6.
 */
public class GetStepsRequest extends BaseRetroRequest<GetStepsModel,Drone> {
    String userID;

    public GetStepsRequest(String userID) {
        super(GetStepsModel.class, Drone.class);
        this.userID = userID;
    }

    @Override
    public GetStepsModel loadDataFromNetwork() throws Exception {
        return getService().stepsGet(userID,buildAuthorization(),CONTENT_TYPE);
    }
}
