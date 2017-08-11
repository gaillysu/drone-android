package com.dayton.drone.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by boy on 2016/4/20.
 */
public class SpUtils {

    public static void putBoolean(Context context, String name, boolean values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (CacheConstants.SP_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name, values);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String name, boolean defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences
                (CacheConstants.SP_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(name, defaultValue);
    }

    public static void putIntMethod(Context context, String name, int values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(name, values);
        editor.apply();
    }

    public static int getIntMethod(Context context, String name, int defaultValues) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(name, defaultValues);
    }

    public static void putLongMethod(Context context, String name, long values) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(name, values);
        editor.apply();
    }

    public static long getLongMethod(Context context, String name, long defaultValues) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(name, defaultValues);
    }

    public static void saveHomeCityId(Context context, int cityId) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CacheConstants.HOME_CITY_ID, cityId).apply();
    }

    public static int getHomeCityId(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CacheConstants.HOME_CITY_ID, -1);
    }

    public static void saveSyncTime(Context context, int typeTime) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CacheConstants.SYNC_TIME, typeTime).apply();
    }

    public static int getSyncTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CacheConstants.SYNC_TIME, 0);
    }

    public static void setIsSyncTime(Context context,boolean isSync) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CacheConstants.IS_SYNC_TIME, isSync).apply();
    }

    public static boolean getIsSyncTime(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CacheConstants.IS_SYNC_TIME, false);
    }

    public static void set24HourFormat(Context context,boolean format24Hour) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CacheConstants.IS_24_HOUR_FORMAT, format24Hour).apply();
    }

    public static boolean get24HourFormat(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CacheConstants.IS_24_HOUR_FORMAT, true);
    }

    public static void saveHotKeyEnable(Context context,boolean isEnable) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(CacheConstants.HOT_KEY_ENABLE, isEnable).apply();
    }

    public static boolean getHotKeyEnable(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(CacheConstants.HOT_KEY_ENABLE, false);
    }

    public static void saveHotKey(Context context, int hotKey) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(CacheConstants.HOT_KEY, hotKey).apply();
    }

    public static int getHotKey(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(CacheConstants.HOT_KEY, 0);
    }

    public static void saveBleVersion(Context context, String version) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CacheConstants.BLE_VERSION, version).apply();
    }

    public static String getBleVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CacheConstants.BLE_VERSION, "");
    }

    public static void saveBleNewVersion(Context context, String version) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CacheConstants.BLE_NEW_VERSION, version).apply();
    }

    public static String getBleNewVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(CacheConstants.SP_Name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(CacheConstants.BLE_NEW_VERSION, "");
    }


    public static void printAllConstants(Context context) {
        Log.w("Karl", "Base steps = " + getIntMethod(context, CacheConstants.TODAY_BASESTEP, -1));
        Log.w("Karl", "     steps = " + getIntMethod(context, CacheConstants.TODAY_STEP, -1));
        Log.w("Karl", "     reset = " + getBoolean(context, CacheConstants.TODAY_RESET, false));
        Log.w("Karl", "     Date  = " + getLongMethod(context, CacheConstants.TODAY_DATE, 0));
        Log.w("Karl", "Must Sync  = " + getBoolean(context, CacheConstants.MUST_SYNC_STEPS, false));
    }
}
