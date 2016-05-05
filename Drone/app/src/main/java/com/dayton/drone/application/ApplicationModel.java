package com.dayton.drone.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.dayton.drone.ble.controller.OtaControllerImpl;
import com.dayton.drone.ble.controller.SyncController;
import com.dayton.drone.ble.controller.SyncControllerImpl;
import com.dayton.drone.cloud.SyncActivityManager;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.modle.User;
import com.dayton.drone.retrofit.RetrofitManager;
import com.dayton.drone.retrofit.model.Steps;

import net.medcorp.library.ble.controller.OtaController;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private SyncActivityManager syncActivityManager;
    private User   user;

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
        syncActivityManager = new SyncActivityManager(this);
        user = new User(0);
        user.setDroneUserID("0");//"0" means anyone user
        //TODO read from user table to get the lastest logged in user
        EventBus.getDefault().register(this);
        syncController.startConnect(false);
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

    public SyncActivityManager getSyncActivityManager(){
        return syncActivityManager;
    }

    public User getUser(){return user;}

    @Subscribe
    public void onEvent(LittleSyncEvent event) {
        Steps steps = new Steps();
        steps.setUid(Integer.parseInt(getUser().getDroneUserID()));
        steps.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        steps.setSteps(event.getSteps());
        getSyncActivityManager().launchSyncDailySteps(steps);
    }

}
