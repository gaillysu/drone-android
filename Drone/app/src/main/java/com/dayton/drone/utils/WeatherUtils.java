package com.dayton.drone.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dayton.drone.network.response.model.GetForecastModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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

    /*
    Set<String> todayForecast, see@ GetForecastModel.Forecast to Json string
    every 3 hours, will has a Forecast record, so total of record for today is less than 8
     */
    public static void saveCityWeather(Context context, String name, Set<String> todayForecast)
    {
        SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sp.edit();
        editor.putStringSet(name,todayForecast);
        editor.apply();
    }

    public static List<GetForecastModel.Forecast> getCityWeather(Context context, String name)
    {
        SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        Set<String> records =  sp.getStringSet(name, new HashSet<String>());
        List<GetForecastModel.Forecast> weather = new ArrayList<>();
        for(String record:records)
        {
            GetForecastModel.Forecast forecast = new Gson().fromJson(record, GetForecastModel.Forecast.class);
            weather.add(forecast);
        }
        Comparator<GetForecastModel.Forecast> comparator = new Comparator<GetForecastModel.Forecast>() {
            public int compare(GetForecastModel.Forecast s1, GetForecastModel.Forecast s2) {
                if (s1.getDt() == s2.getDt()) {
                    return 0;
                } else if (s1.getDt() > s2.getDt()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };
        Collections.sort(weather,comparator);

        return weather;
    }

    public static void saveCityWeatherFirstForecastDateTime(Context context, String name, long firstForecastDateTime)
    {
        SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sp.edit();
        editor.putLong(name+"first",firstForecastDateTime);
        editor.apply();
    }

    public static Long getCityWeatherFirstForecastDateTime(Context context, String name)
    {
        SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        return sp.getLong(name+"first", 0l);
    }


}
