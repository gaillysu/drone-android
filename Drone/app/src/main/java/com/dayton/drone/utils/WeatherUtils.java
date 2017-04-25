package com.dayton.drone.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dayton.drone.network.response.model.Forecast;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
        if(name.contains(",")) {
            name = name.split(",")[0];
        }
        List<String> cities = getWeatherCities(context);
        if(!cities.contains(name)){
            SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor =  sp.edit();
            ArrayList<String> cityList = new ArrayList<>(cities);
            cityList.add(name);
            editor.putString(CITYLIST,cityList.toString().replace("[","").replace("]",""));
            editor.apply();
        }
    }

    public static List<String> getWeatherCities(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        String cites = sp.getString(CITYLIST, new String());
        if(cites.isEmpty()) {
            return new ArrayList<String>();
        }
        String [] cityArray = cites.split(",");
        return Arrays.asList(cityArray);
    }

    public static void removeAllCities(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sp.edit();
        editor.putString(CITYLIST,new String());
        editor.apply();
    }

    public static int getWeatherLocationId(Context context, String name)
    {
        int index = 0;
        List<String> cities = getWeatherCities(context);
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

    public static List<Forecast> getCityWeather(Context context, String name)
    {
        SharedPreferences sp = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        Set<String> records =  sp.getStringSet(name, new HashSet<String>());
        List<Forecast> weather = new ArrayList<>();
        for(String record:records)
        {
            Forecast forecast = new Gson().fromJson(record, Forecast.class);
            weather.add(forecast);
        }
        Comparator<Forecast> comparator = new Comparator<Forecast>() {
            public int compare(Forecast s1, Forecast s2) {
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
