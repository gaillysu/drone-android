package com.dayton.drone.network.request;

import android.util.Log;

import com.dayton.drone.network.request.model.CreateUser;
import com.dayton.drone.network.request.model.UpdateUser;
import com.dayton.drone.network.request.model.UpdateUserObject;
import com.dayton.drone.network.request.model.UpdateUserParameters;
import com.dayton.drone.network.response.model.UpdateUserModel;
import com.dayton.drone.network.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by med on 16/5/16.
 */
public class UpdateUserRequest extends BaseRequest<UpdateUserModel,Drone> implements BaseRequest.BaseRetroRequestBody<UpdateUserObject>{

    private String token;
    private UpdateUser user;

    public UpdateUserRequest(UpdateUser user,String token) {
        super(UpdateUserModel.class, Drone.class);
        this.user = user;
        this.token = token;
    }

    @Override
    public UpdateUserModel loadDataFromNetwork() throws Exception {
        return getService().userUpdate(buildRequestBody(),buildAuthorization(),CONTENT_TYPE);
    }

    @Override
    public UpdateUserObject buildRequestBody() {
        UpdateUserObject object = new UpdateUserObject();
        object.setToken(token);
        UpdateUserParameters parameters = new UpdateUserParameters();
        parameters.setUser(user);
        object.setParams(parameters);
        Log.i(this.getClass().getSimpleName(), "object: " + new Gson().toJson(object));
        return object;
    }
}
