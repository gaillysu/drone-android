package com.dayton.drone.network.request.model;

/**
 * Created by Administrator on 2016/6/8.
 */
public class RequestChangePasswordBody {

    private String token ;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RequestChangePasswordBodyParameters getParms() {
        return parms;
    }

    public void setParms(RequestChangePasswordBodyParameters parms) {
        this.parms = parms;
    }

    private RequestChangePasswordBodyParameters parms;

    public RequestChangePasswordBody(){

    }


    public RequestChangePasswordBody(String token, RequestChangePasswordBodyParameters parms){
        this.parms = parms;
        this.token = token;
    }
}
