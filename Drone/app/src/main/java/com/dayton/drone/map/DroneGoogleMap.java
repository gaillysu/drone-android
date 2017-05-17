package com.dayton.drone.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.dayton.drone.map.request.Request;
import com.dayton.drone.view.GoogleMapView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;

/**
 * Created by med on 17/5/16.
 */

public class DroneGoogleMap implements BaseMap,OnMapReadyCallback {

    private Context context;
    private GoogleMapView googleMapView;
    private GoogleMap googleMap;

    public DroneGoogleMap(Context context) {
        this.context = context;
        this.googleMapView = new GoogleMapView(context, this);
    }

    @Override
    public View getMap() {
        return googleMapView;
    }

    @Override
    public void followGPS(boolean follow) {
        if(googleMap == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.setIndoorEnabled(false);
        googleMap.setBuildingsEnabled(false);
        googleMap.setMyLocationEnabled(follow);

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if(location!=null) {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(location.getLatitude(), location.getLongitude()))
                    .zoom(17).build()));
        }
        else {
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(0, 0))
                    .zoom(1).build()));
        }
    }

    @Override
    public void searchPOI(final Request request) {
        //start a new thread to get the Address list
        new Thread(new Runnable() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context);
                try {
                    final List<Address> result =  geocoder.getFromLocationName(request.getInput(),5);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            request.onSuccess(result);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    request.onError(e);
                }
            }
        }).start();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        followGPS(true);
    }

}
