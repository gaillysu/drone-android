package com.dayton.drone.map.listener;

import org.json.JSONObject;

/**
 * Created by med on 17/5/16.
 */

public interface ResponseListener<T> {
    public void onSuccess(T result);
    public void onError(Exception error);
}
