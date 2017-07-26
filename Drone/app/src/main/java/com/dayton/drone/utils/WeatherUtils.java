package com.dayton.drone.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import net.medcorp.library.worldclock.City;

import java.util.Calendar;
import java.util.List;

/**
 * Created by med on 17/4/18.
 *
 */

public class WeatherUtils {

    public static String getLocalCityName() {
        String localCity = Calendar.getInstance().getTimeZone().getID().split("/")[1].replace("_", " ");
        return localCity;
    }

    public static Location getLocalCityLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        return location;
    }
}
