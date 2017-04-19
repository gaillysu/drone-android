package com.dayton.drone.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by med on 17/4/18.
 */

public class WeatherUtils {
    final static String CITYLIST = "CITYLIST";

    //when user select/unselect a city, update the city list

    public static void addWeatherCity(Context context, String name)
    {
        Set<String> cities = getWeatherCities(context);
        if(!cities.contains(name)){
            SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =  sp.edit();
            cities.add(name);
            editor.putStringSet(CITYLIST,cities);
            editor.apply();
        }
    }

    public static Set<String> getWeatherCities(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        return sp.getStringSet(CITYLIST, new HashSet<String>());
    }

    public static void removeWeatherCity(Context context, String name)
    {
        Set<String> cities = getWeatherCities(context);
        if(cities.contains(name)){
            SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =  sp.edit();
            cities.remove(name);
            editor.putStringSet(CITYLIST,cities);
            editor.apply();
        }
    }

    public static void removeAllCities(Context context)
    {
        Set<String> cities = getWeatherCities(context);
        SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sp.edit();
        cities.clear();
        editor.putStringSet(CITYLIST,cities);
        editor.apply();
    }

    public static int getWeatherLocationId(Context context, String name)
    {
        int index = 0;
        Set<String> cities = getWeatherCities(context);
        for(String city:cities){
            if(name.equals(city)){
                return index;
            }
            index++;
        }
        return -1;
    }

}
