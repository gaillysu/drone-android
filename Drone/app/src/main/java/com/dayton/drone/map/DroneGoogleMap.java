package com.dayton.drone.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.dayton.drone.R;
import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.map.request.Request;
import com.dayton.drone.network.request.GetGeocodeRequest;
import com.dayton.drone.network.response.model.GetGeocodeModel;
import com.dayton.drone.network.response.model.Route;
import com.dayton.drone.network.response.model.Step;
import com.dayton.drone.view.GoogleMapView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.IOException;
import java.util.List;

/**
 * Created by med on 17/5/16.
 */

public class DroneGoogleMap implements BaseMap,OnMapReadyCallback {

    private Context context;
    private GoogleMapView googleMapView;
    private GoogleMap googleMap;
    private Location location;

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
        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if(location!=null) {
            zoomTo(location.getLatitude(), location.getLongitude());
        }
        else {
            zoomTo(0,0);
        }
    }

    @Override
    public Location getLocalLocation() {
        return location;
    }

    @Override
    public void renderRouteMap(Route[] routes) {
        googleMap.clear();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(12);
        polylineOptions.color(Color.BLUE);
        for(Route route:routes){
             if(route.getLegs().length>0) {
                 //only select the first Leg for every route
                 Step[] steps = route.getLegs()[0].getSteps();
                 if(steps.length>0)
                 {
                     for (int i = 0; i < steps.length; i++) {
                         if (i == 0) {
                             googleMap.addMarker(new MarkerOptions()
                                     .position(new LatLng(steps[i].getStart_location().getLat(), steps[i].getStart_location().getLng()))
                                     .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_icon_map_start)));
                         }
                         if (i == route.getLegs()[0].getSteps().length - 1) {
                             googleMap.addMarker(new MarkerOptions()
                                     .position(new LatLng(steps[i].getEnd_location().getLat(), steps[i].getEnd_location().getLng()))
                                     .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_icon_map_stop)));
                         }
                         polylineOptions.add(new LatLng(steps[i].getStart_location().getLat(), steps[i].getStart_location().getLng()));
                         polylineOptions.add(new LatLng(steps[i].getEnd_location().getLat(), steps[i].getEnd_location().getLng()));
                     }
                     googleMap.addPolyline(polylineOptions);
                 }
             }
        }
    }

    @Override
    public void zoomTo(double latitude, double longitude) {
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(new LatLng(latitude, longitude))
                .zoom(17).build()));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        followGPS(true);
    }

}
