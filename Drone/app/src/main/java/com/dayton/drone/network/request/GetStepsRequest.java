package com.dayton.drone.network.request;

import com.dayton.drone.network.response.model.GetStepsModel;
import com.dayton.drone.network.restapi.Drone;

/**
 * Created by med on 16/5/6.
 */
public class GetStepsRequest extends BaseRequest<GetStepsModel,Drone> {
    String userID;
    String token;
    String start_date;
    String end_date;

    public GetStepsRequest(String userID,String token,String start_date,String end_date) {
        super(GetStepsModel.class, Drone.class);
        this.userID = userID;
        this.token = token;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    @Override
    public GetStepsModel loadDataFromNetwork() throws Exception {
        return getService().stepsGet(userID,token,start_date,end_date,buildAuthorization(),CONTENT_TYPE);
    }
}
