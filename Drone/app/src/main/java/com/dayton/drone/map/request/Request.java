package com.dayton.drone.map.request;

import org.json.JSONObject;

/**
 * Created by med on 17/5/16.
 */

public interface Request<T> {
    String  getInput();
    void onSuccess(T response);
    void onError(Exception exception);
}
