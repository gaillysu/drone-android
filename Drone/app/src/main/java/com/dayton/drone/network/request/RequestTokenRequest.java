package com.dayton.drone.network.request;

import android.util.Log;

import com.dayton.drone.network.request.model.RequestTokenBody;
import com.dayton.drone.network.request.model.RequestTokenBodyParameters;
import com.dayton.drone.network.response.model.RequestTokenResponse;
import com.dayton.drone.network.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2016/6/8.
 */
public class RequestTokenRequest extends BaseRequest<RequestTokenResponse,Drone> implements BaseRequest.BaseRetroRequestBody<RequestTokenBody> {

    private String token;
    private String email;

    public RequestTokenRequest(String token, String email) {
        super(RequestTokenResponse.class, Drone.class);
        this.token = token;
        this.email = email;
    }

    @Override
    public RequestTokenResponse loadDataFromNetwork() throws Exception {
        return getService().requestToken(buildRequestBody(), buildAuthorization(), CONTENT_TYPE);
    }

    @Override
    public RequestTokenBody buildRequestBody() {
        RequestTokenBodyParameters parameters = new RequestTokenBodyParameters(email);
        RequestTokenBody body =  new RequestTokenBody(token,parameters);
        Log.i("Karl", "object: " + new Gson().toJson(body));
        return body;
    }
}
