package com.dayton.drone.network.request;

import android.util.Log;

import com.dayton.drone.network.request.model.ChangePasswordModel;
import com.dayton.drone.network.request.model.RequestChangePasswordBody;
import com.dayton.drone.network.request.model.RequestChangePasswordBodyParameters;
import com.dayton.drone.network.response.model.RequestChangePasswordResponse;
import com.dayton.drone.network.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/6/8.
 */
public class RequestChangePasswordRequest extends BaseRequest<RequestChangePasswordResponse,Drone>
        implements BaseRequest.BaseRetroRequestBody<RequestChangePasswordBody>

{
    private ChangePasswordModel changePasswordModel;
    private String token;

    public RequestChangePasswordRequest(String token,ChangePasswordModel changePasswordModel) {
        super(RequestChangePasswordResponse.class, Drone.class);

        this.token = token;
        this.changePasswordModel = changePasswordModel;
    }


    @Override
    public RequestChangePasswordBody buildRequestBody() {


        RequestChangePasswordBodyParameters parameters = new RequestChangePasswordBodyParameters(token,changePasswordModel);
        RequestChangePasswordBody body = new RequestChangePasswordBody();
        body.setToken(token);
        body.setParms(parameters);
        Log.i("Karl", "object: " + new Gson().toJson(body));
        return body;
    }

    @Override
    public RequestChangePasswordResponse loadDataFromNetwork() throws Exception {
        return getService().requestChangePasswor(buildRequestBody() ,buildAuthorization(), CONTENT_TYPE);
    }
}
