package com.dayton.drone.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.dayton.drone.Constance;

/**
 * Created by boy on 2016/4/20.
 */
public class SpUtils {

    public static void putBoolean(Context context,String name ,boolean values){
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (Constance.SP_Name,Context.MODE_PRIVATE);
       SharedPreferences.Editor editor =  sharedPreferences.edit();
        editor.putBoolean(name,values);
        editor.commit();
    }

    public static boolean getBoolean(Context context,String name, boolean defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (Constance.SP_Name,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(name,defaultValue);
    }
}
