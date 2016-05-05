package com.dayton.drone.retrofit.request.user;

import android.util.Log;

import com.dayton.drone.retrofit.model.UserLogin;
import com.dayton.drone.retrofit.restapi.Drone;
import com.dayton.drone.retrofit.request.BaseRetroRequest;
import com.google.gson.Gson;

/**
 * Created by med on 16/5/3.
 */
public class LoginUserRequest extends BaseRetroRequest<LoginUserModel,Drone> implements BaseRetroRequest.BaseRetroRequestBody<LoginUserObject> {

    private String token;
    private UserLogin user;

    public LoginUserRequest(UserLogin user, String token) {
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
        LoginParameters parameters = new LoginParameters();
        parameters.setUser(user);
        object.setParams(parameters);
        Log.i(this.getClass().getSimpleName(), "object: " + new Gson().toJson(object));
        return object;
    }
}
