package com.dayton.drone.map;

import android.os.Bundle;
import android.view.View;

import com.dayton.drone.map.request.Request;

/**
 * Created by med on 17/5/15.
 */

public interface BaseMap {

    View getMap();

    void followGPS(boolean follow);

    void searchPOI(Request request);
}
