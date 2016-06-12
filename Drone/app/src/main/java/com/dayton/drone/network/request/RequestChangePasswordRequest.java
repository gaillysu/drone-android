package com.dayton.drone.network.request;

import android.util.Log;

import com.dayton.drone.network.request.model.ChangePasswordModel;
import com.dayton.drone.network.request.model.ChangePasswordObject;
import com.dayton.drone.network.request.model.ChangePasswordUserParameters;
import com.dayton.drone.network.response.model.RequestChangePasswordResponse;
import com.dayton.drone.network.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/6/8.
 */
public class RequestChangePasswordRequest extends BaseRequest<RequestChangePasswordResponse,Drone>
        implements BaseRequest.BaseRetroRequestBody<ChangePasswordObject>

{
    private ChangePasswordModel changePasswordModel;
    private String token;

    public RequestChangePasswordRequest(String token,ChangePasswordModel changePasswordModel) {
        super(RequestChangePasswordResponse.class, Drone.class);

        this.token = token;
        this.changePasswordModel = changePasswordModel;
    }


    @Override
    public ChangePasswordObject buildRequestBody() {
        ChangePasswordObject object = new ChangePasswordObject();
        object.setToken(token);
        ChangePasswordUserParameters parameters = new ChangePasswordUserParameters();
        parameters.setUser(changePasswordModel);
        object.setParams(parameters);
        Log.i(this.getClass().getSimpleName(),"object:" + new Gson().toJson(object));
        return object;
    }

    @Override
    public RequestChangePasswordResponse loadDataFromNetwork() throws Exception {
        return getService().requestChangePasswor(buildRequestBody() ,buildAuthorization(), CONTENT_TYPE);
    }
}
