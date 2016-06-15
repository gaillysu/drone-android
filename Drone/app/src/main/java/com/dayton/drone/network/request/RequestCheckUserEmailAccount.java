package com.dayton.drone.network.request;

import android.util.Log;

import com.dayton.drone.network.request.model.CheckEmailResponse;
import com.dayton.drone.network.request.model.CheckEmailUserBody;
import com.dayton.drone.network.request.model.CheckEmailUserParameters;
import com.dayton.drone.network.request.model.CheckUserEmailObject;
import com.dayton.drone.network.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/6/15.
 */
public class RequestCheckUserEmailAccount extends BaseRequest<CheckEmailResponse, Drone> implements BaseRequest.BaseRetroRequestBody
        <CheckUserEmailObject> {
    private String token;
    private CheckEmailUserBody user;


    public RequestCheckUserEmailAccount(String token, CheckEmailUserBody user) {
        super(CheckEmailResponse.class, Drone.class);
        this.token = token;
        this.user = user;
    }

    @Override
    public CheckUserEmailObject buildRequestBody() {
        CheckEmailUserParameters parameters = new CheckEmailUserParameters(user);
        CheckUserEmailObject object = new CheckUserEmailObject();
        object.setToken(token);
        object.setParams(parameters);
        Log.i(this.getClass().getSimpleName(), "object: " + new Gson().toJson(object));
        return object;
    }

    @Override
    public CheckEmailResponse loadDataFromNetwork() throws Exception {
        return getService().requestCheckEmailResponse(buildRequestBody(), buildAuthorization(), CONTENT_TYPE);
    }
}
