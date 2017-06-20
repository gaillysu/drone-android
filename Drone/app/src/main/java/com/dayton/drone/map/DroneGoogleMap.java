package com.dayton.drone.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.dayton.drone.R;
import com.dayton.drone.network.response.model.Route;
import com.dayton.drone.network.response.model.Step;
import com.dayton.drone.view.GoogleMapView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

/**
 * Created by med on 17/5/16.
 */

public class DroneGoogleMap implements BaseMap, OnMapReadyCallback, LocationListener {

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
        if (googleMap == null) {
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
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (location != null) {
            zoomTo(location.getLatitude(), location.getLongitude());
        } else {
            zoomTo(0, 0);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, this);
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
        polylineOptions.color(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        for (Route route : routes) {
            if (route.getLegs().length > 0) {
                //only select the first Leg for every route
                Step[] steps = route.getLegs()[0].getSteps();
                if (steps.length > 0) {
                    for (int i = 0; i < steps.length; i++) {
                        if (i == 0) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(steps[i].getStartLocation().getLat(), steps[i].getStartLocation().getLng()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_icon_map_start)));
                        }
                        if (i == route.getLegs()[0].getSteps().length - 1) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(steps[i].getEndLocation().getLat(), steps[i].getEndLocation().getLng()))
                                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_icon_map_stop)));
                        }
                        polylineOptions.add(new LatLng(steps[i].getStartLocation().getLat(), steps[i].getStartLocation().getLng()));
                        //add more geo points in this step line
                        if(steps[i].getPolyline()!=null && steps[i].getPolyline().getPoints()!=null) {
                            ArrayList<LatLng> points = decodePolyPoints(steps[i].getPolyline().getPoints());
                            for(LatLng latLng:points) {
                                polylineOptions.add(latLng);
                            }
                        }
                        polylineOptions.add(new LatLng(steps[i].getEndLocation().getLat(), steps[i].getEndLocation().getLng()));
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


    @Override
    public void onLocationChanged(Location location) {
        Log.i("location",location.getProvider() + " location changed: " + location.getLatitude() + "," + location.getLongitude()+ ",Accuracy: " + location.getAccuracy());
        this.location = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private ArrayList<LatLng> decodePolyPoints(String encodedPath){
        int len = encodedPath.length();

        final ArrayList<LatLng> path = new ArrayList<LatLng>();
        int index = 0;
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int result = 1;
            int shift = 0;
            int b;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            result = 1;
            shift = 0;
            do {
                b = encodedPath.charAt(index++) - 63 - 1;
                result += b << shift;
                shift += 5;
            } while (b >= 0x1f);
            lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

            path.add(new LatLng(lat * 1e-5, lng * 1e-5));
        }

        return path;
    }
}
