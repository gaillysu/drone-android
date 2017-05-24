package com.dayton.drone.map.request;

import android.location.Address;

import com.dayton.drone.map.listener.ResponseListener;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by med on 17/5/16.
 */

public class GeoRequest<T> implements Request<T> {

   final private String address;
   final  private ResponseListener<T> callback;

    public GeoRequest(String address, ResponseListener<T> callback) {
        this.address = address;
        this.callback = callback;
    }

    @Override
    public String getInput() {
        return address;
    }

    @Override
    public void onSuccess(T response) {
        callback.onSuccess(response);
    }

    @Override
    public void onError(Exception exception) {
        callback.onError(exception);
    }
}
