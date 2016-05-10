package com.dayton.drone.network.request;

import android.util.Log;

import com.dayton.drone.network.request.model.CreateUser;
import com.dayton.drone.network.request.model.CreateUserObject;
import com.dayton.drone.network.request.model.CreateUserParameters;
import com.dayton.drone.network.response.model.CreateUserModel;
import com.dayton.drone.network.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by med on 16/5/3.
 */
public class CreateUserRequest extends BaseRequest<CreateUserModel,Drone> implements BaseRequest.BaseRetroRequestBody<CreateUserObject> {

    private String token;
    private CreateUser   user;

    public CreateUserRequest(CreateUser user,String token) {
        super(CreateUserModel.class, Drone.class);
        this.user  = user;
        this.token = token;
    }

    @Override
    public CreateUserModel loadDataFromNetwork() throws Exception {
        return getService().userCreate(buildRequestBody(), buildAuthorization(), CONTENT_TYPE);
    }

    @Override
    public CreateUserObject buildRequestBody() {
        CreateUserObject object = new CreateUserObject();
        object.setToken(token);
        CreateUserParameters parameters = new CreateUserParameters();
        parameters.setUser(user);
        object.setParams(parameters);
        Log.i(this.getClass().getSimpleName(), "object: " + new Gson().toJson(object));
        return object;
    }
}
