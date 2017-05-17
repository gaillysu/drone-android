package com.dayton.drone.map;

import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.dayton.drone.map.request.Request;
import com.dayton.drone.network.response.model.Route;

/**
 * Created by med on 17/5/15.
 */

public interface BaseMap {

    View getMap();

    void followGPS(boolean follow);

    void searchPOI(Request request);

    Location getLocalLocation();

    void renderRouteMap(Route[] route);
}
