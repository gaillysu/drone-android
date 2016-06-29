package com.dayton.drone.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by boy on 2016/4/20.
 */
public class SpUtils {

    public static void putBoolean(Context context,String name ,boolean values){
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (CacheConstants.SP_Name,Context.MODE_PRIVATE);
       SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putBoolean(name,values);
        editor.apply();
    }

    public static boolean getBoolean(Context context,String name, boolean defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (CacheConstants.SP_Name,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(name,defaultValue);
    }

    public static void putIntMethod(Context context,String name,int values){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putInt(name,values);
        editor.apply();
    }

    public static int getIntMethod(Context context,String name, int defaultValues){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(name,defaultValues);
    }

    public static void putLongMethod(Context context,String name,long values){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putLong(name,values);
        editor.apply();
    }

    public static long getLongMethod(Context context,String name, long defaultValues){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name,Context.MODE_PRIVATE);
        return sharedPreferences.getLong(name,defaultValues);
    }
}
