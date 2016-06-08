package com.dayton.drone.network.request;

import android.util.Log;

import com.dayton.drone.network.request.model.RequestChangePasswordBody;
import com.dayton.drone.network.request.model.RequestChangePasswordBodyParameters;
import com.dayton.drone.network.response.model.RequestChangePasswordResponse;
import com.dayton.drone.network.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/6/8.
 */
public class RequestChangePasswordRequest extends BaseRequest<RequestChangePasswordResponse,Drone> implements BaseRequest.BaseRetroRequestBody<RequestChangePasswordBody>

{
    private String token;
    private String email;
    private int id;
    private String password;

    public RequestChangePasswordRequest(String token, String email, String password, int id) {
        super(RequestChangePasswordResponse.class, Drone.class);
        this.email = email;
        this.id = id;
        this.token = token;
        this.password = password;
    }


    @Override
    public RequestChangePasswordBody buildRequestBody() {
        RequestChangePasswordBodyParameters parameters = new RequestChangePasswordBodyParameters(email,token,password,id);
        RequestChangePasswordBody body = new RequestChangePasswordBody(token,parameters);
        Log.i("Karl", "object: " + new Gson().toJson(body));
        return body;
    }

    @Override
    public RequestChangePasswordResponse loadDataFromNetwork() throws Exception {
        return getService().requestChangePasswor(buildRequestBody() ,buildAuthorization(), CONTENT_TYPE);
    }
}