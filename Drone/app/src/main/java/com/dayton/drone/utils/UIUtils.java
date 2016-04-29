package com.dayton.drone.utils;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.dayton.drone.application.ApplicationModel;

/**
 * Created by boy on 2016/4/27.
 */
public class UIUtils {


    public static Context getContext() {
        return ApplicationModel.getContext();
    }

    public static Resources getResources() {
        return getContext().getResources();
    }


    public static String getString(int resId) {
        return getContext().getResources().getString(resId);
    }

    public static String getString(int resId, Object... formatArgs) {
        return getContext().getResources().getString(resId, formatArgs);
    }


    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }


    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public static Handler getMainHandler() {
        return ApplicationModel.getHandler();
    }


    public static void post(Runnable task) {
        getMainHandler().post(task);
    }


    public static void postDelayed(Runnable task, long delayMillis) {
        getMainHandler().postDelayed(task, delayMillis);
    }


    public static void removeCallbacks(Runnable task) {
        getMainHandler().removeCallbacks(task);
    }


    public static int px2dp(int px) {
        // px = dp * (dpi / 160)
        // dp = px * 160 / dpi

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;
        return (int) (px * 160f / dpi + 0.5f);
    }

    public static int dp2px(int dp) {
        // px = dp * (dpi / 160)
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;

        return (int) (dp * (dpi / 160f) + 0.5f);
    }


    public static String getPackageName() {
        return getContext().getPackageName();
    }
}



