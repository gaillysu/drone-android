package com.dayton.drone.application;

import android.app.Application;

import com.dayton.drone.ble.controller.SyncController;
import com.dayton.drone.ble.controller.SyncControllerImpl;
import com.dayton.drone.cloud.SyncActivityManager;
import com.dayton.drone.database.entry.NotificationDatabaseHelper;
import com.dayton.drone.database.entry.StepsDatabaseHelper;
import com.dayton.drone.database.entry.UserDatabaseHelper;
import com.dayton.drone.database.entry.WatchesDatabaseHelper;
import com.dayton.drone.database.entry.WorldClockDatabaseHelper;
import com.dayton.drone.model.User;
import com.dayton.drone.network.RetrofitManager;

import net.medcorp.library.ble.util.Optional;

/**
 * Created by karl-john on 18/3/16.
 */
public class ApplicationModel extends Application {
    private SyncController syncController;
    private RetrofitManager retrofitManager;
    private SyncActivityManager syncActivityManager;
    private User   user;
    private UserDatabaseHelper userDatabaseHelper;
    private WorldClockDatabaseHelper worldClockDatabaseHelper;
    private StepsDatabaseHelper stepsDatabaseHelper;
    private NotificationDatabaseHelper notificationDatabaseHelper;
    private WatchesDatabaseHelper watchesDatabaseHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        syncController = new SyncControllerImpl(this);
        retrofitManager = new RetrofitManager(this);
        syncActivityManager = new SyncActivityManager(this);
        userDatabaseHelper = new UserDatabaseHelper(this);
        worldClockDatabaseHelper = new WorldClockDatabaseHelper(this);
        stepsDatabaseHelper = new StepsDatabaseHelper(this);
        notificationDatabaseHelper = new NotificationDatabaseHelper(this);
        watchesDatabaseHelper = new WatchesDatabaseHelper(this);
        Optional<User> loginUser = userDatabaseHelper.getLoginUser();
        if(loginUser.notEmpty()) {
            user = loginUser.get();
        }
        else {
            user = new User();
            user.setUserID("0");//"0" means anyone user
        }
    }

    public SyncController getSyncController() {
        return syncController;
    }

    public RetrofitManager getRetrofitManager(){
        return retrofitManager;
    }

    public SyncActivityManager getSyncActivityManager(){
        return syncActivityManager;
    }

    public User getUser(){return user;}

    public StepsDatabaseHelper getStepsDatabaseHelper() {
        return stepsDatabaseHelper;
    }
    public UserDatabaseHelper getUserDatabaseHelper() {
        return userDatabaseHelper;
    }
    public WorldClockDatabaseHelper getWorldClockDatabaseHelper() {
        return worldClockDatabaseHelper;
    }
    public NotificationDatabaseHelper getNotificationDatabaseHelper(){
        return notificationDatabaseHelper;
    }
    public WatchesDatabaseHelper getWatchesDatabaseHelper(){
        return watchesDatabaseHelper;
    }


}
