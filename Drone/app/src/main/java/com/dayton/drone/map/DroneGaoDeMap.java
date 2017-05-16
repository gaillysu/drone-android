package com.dayton.drone.map;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.dayton.drone.map.request.Request;

/**
 * Created by med on 17/5/15.
 */

public class DroneGaoDeMap implements BaseMap {

    private Context context;
    private MapView mapView;
    private AMapLocationListener externalAMapLocationListener;

    public DroneGaoDeMap(Context context) {
        this.context = context;
        this.mapView = new MapView(context);
        UiSettings settings = mapView.getMap().getUiSettings();
        settings.setZoomControlsEnabled(false);
        settings.setCompassEnabled(true);
        settings.setScaleControlsEnabled(true);
        settings.setMyLocationButtonEnabled(true);
        mapView.getMap().setMyLocationEnabled(true);
        mapView.getMap().animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public View getMap() {
        return mapView;
    }

    @Override
    public void followGPS(boolean follow) {

    }

    @Override
    public void searchPOI(Request request) {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        mapView.onResume();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
    }

    @Override
    public void onPause() {
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mapView.onSaveInstanceState(outState);
    }


}
