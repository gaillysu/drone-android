package com.dayton.drone.map.builder;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocationListener;
import com.dayton.drone.map.BaseMap;
import com.dayton.drone.map.GaoDeMap;

import java.util.Locale;

/**
 * Created by med on 17/5/15.
 */

public class MapBuilder {

    private Context context;
    private ViewGroup  mapViewLayout;
    private final BaseMap map;

    public MapBuilder(ViewGroup viewGroup, Context context){
        this.context = context;
        mapViewLayout = viewGroup;
        map = new GaoDeMap(context);
        mapViewLayout.addView(map.getMap());
        map.getMap().setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT));
    }
    public BaseMap build(Bundle bundle){
             map.onCreate(bundle);
             return map;
    }
}
