package com.dayton.drone.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.dayton.drone.ble.controller.OtaControllerImpl;
import com.dayton.drone.ble.controller.SyncController;
import com.dayton.drone.ble.controller.SyncControllerImpl;
import com.dayton.drone.retrofit.RetrofitManager;

import net.medcorp.library.ble.controller.OtaController;

/**
 * Created by karl-john on 18/3/16.
 */
public class ApplicationModel extends Application {

    private static Context mContext;
    private static Thread	mMainThread;
    private static long		mMainThreadId;
    private static Looper	mMainThreadLooper;
    private static Handler mHandler;

    private SyncController syncController;
    private OtaController otaController;
    private RetrofitManager retrofitManager;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mContext = this;

        mHandler = new Handler();

        mMainThread = Thread.currentThread();

        mMainThreadId = android.os.Process.myTid();

        mMainThreadLooper = getMainLooper();

        syncController = new SyncControllerImpl(this);
        otaController  = new OtaControllerImpl(this);
        retrofitManager = new RetrofitManager(this);
    }

    public static Context getContext()
    {
        return mContext;
    }

    public static Handler getHandler()
    {
        return mHandler;
    }

    public static Thread getMainThread()
    {
        return mMainThread;
    }

    public static long getMainThreadId()
    {
        return mMainThreadId;
    }

    public static Looper getMainThreadLooper()
    {
        return mMainThreadLooper;
    }


    public SyncController getSyncController() {
        return syncController;
    }

    public OtaController getOtaController() {
        return otaController;
    }

    public RetrofitManager getRetrofitManager(){
        return retrofitManager;
    }

}
