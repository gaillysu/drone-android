package com.dayton.drone.network.request;

import com.dayton.drone.network.response.model.GetStepsModel;
import com.dayton.drone.network.restapi.Drone;

/**
 * Created by med on 16/5/6.
 */
public class GetStepsRequest extends BaseRequest<GetStepsModel,Drone> {
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
