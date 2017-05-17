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

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.map.request.Request;
import com.dayton.drone.network.request.GetGeocodeRequest;
import com.dayton.drone.network.response.model.GetGeocodeModel;
import com.dayton.drone.network.response.model.Route;
import com.dayton.drone.view.GoogleMapView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by med on 17/5/16.
 */

public class DroneGoogleMap implements BaseMap,OnMapReadyCallback {

    private ApplicationModel applicationModel;
    private GoogleMapView googleMapView;
    private GoogleMap googleMap;
    private Location location;

    public DroneGoogleMap(ApplicationModel applicationModel) {
        this.applicationModel = applicationModel;
        this.googleMapView = new GoogleMapView(applicationModel, this);
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
        if (ActivityCompat.checkSelfPermission(applicationModel, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationModel, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
        googleMap.setIndoorEnabled(false);
        googleMap.setBuildingsEnabled(false);
        googleMap.setMyLocationEnabled(follow);

        LocationManager locationManager = (LocationManager) applicationModel.getSystemService(Context.LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
        GetGeocodeRequest getGeocodeRequest = new GetGeocodeRequest(request.getInput(),applicationModel.getRetrofitManager().getGoogleMapApiKey());
        applicationModel.getRetrofitManager().executeGoogleMapApi(getGeocodeRequest, new RequestListener<GetGeocodeModel>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                request.onError(spiceException);
            }

            @Override
            public void onRequestSuccess(GetGeocodeModel getGeocodeModel) {
                request.onSuccess(getGeocodeModel);
            }
        });
    }

    @Override
    public Location getLocalLocation() {
        return location;
    }

    @Override
    public void renderRouteMap(Route[] route) {
        //TODO  render google map
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        followGPS(true);
    }

}
