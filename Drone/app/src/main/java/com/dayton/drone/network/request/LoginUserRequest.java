package com.dayton.drone.network.request;

import android.util.Log;

import com.dayton.drone.network.request.model.LoginUser;
import com.dayton.drone.network.request.model.LoginUserParameters;
import com.dayton.drone.network.request.model.LoginUserObject;


import com.dayton.drone.network.response.model.LoginUserModel;
import com.dayton.drone.network.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by med on 16/5/3.
 */
public class LoginUserRequest extends BaseRequest<LoginUserModel,Drone> implements BaseRequest.BaseRetroRequestBody<LoginUserObject> {

    private String token;
    private LoginUser user;

    public LoginUserRequest(LoginUser user, String token) {
        super(LoginUserModel.class, Drone.class);
        this.user  = user;
        this.token = token;
    }

    @Override
    public LoginUserModel loadDataFromNetwork() throws Exception {
        return getService().userLogin(buildRequestBody(), buildAuthorization(),CONTENT_TYPE);
    }

    @Override
    public LoginUserObject buildRequestBody() {
        LoginUserObject object = new LoginUserObject();
        object.setToken(token);
        LoginUserParameters parameters = new LoginUserParameters();
        parameters.setUser(user);
        object.setParams(parameters);
        Log.i(this.getClass().getSimpleName(), "object: " + new Gson().toJson(object));
        return object;
    }
}
