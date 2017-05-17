package com.dayton.drone.map.builder;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.map.BaseMap;
import com.dayton.drone.map.DroneGoogleMap;

/**
 * Created by med on 17/5/15.
 */

public class MapBuilder {

    private ApplicationModel applicationModel;
    private ViewGroup  mapViewLayout;
    private BaseMap map;

    public MapBuilder(ViewGroup viewGroup, ApplicationModel applicationModel){
        this.applicationModel = applicationModel;
        mapViewLayout = viewGroup;
    }
    public BaseMap build(Bundle bundle){
        if(map==null) {
            map = new DroneGoogleMap(applicationModel);
            mapViewLayout.addView(map.getMap());
            map.getMap().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        }
        return map;
    }
}
