package com.dayton.drone.application;

import android.app.Application;

import com.dayton.drone.ble.controller.OtaControllerImpl;
import com.dayton.drone.ble.controller.SyncController;
import com.dayton.drone.ble.controller.SyncControllerImpl;
import com.dayton.drone.cloud.SyncActivityManager;
import com.dayton.drone.database.entry.NotificationDatabaseHelper;
import com.dayton.drone.database.entry.SleepDatabaseHelper;
import com.dayton.drone.database.entry.StepsDatabaseHelper;
import com.dayton.drone.database.entry.UserDatabaseHelper;
import com.dayton.drone.database.entry.WatchesDatabaseHelper;
import com.dayton.drone.database.entry.WorldClockDatabaseHelper;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.model.User;
import com.dayton.drone.network.RetrofitManager;
import com.dayton.drone.network.request.model.CreateSteps;

import net.medcorp.library.ble.controller.OtaController;
import net.medcorp.library.ble.util.Optional;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by karl-john on 18/3/16.
 */
public class ApplicationModel extends Application {
    private SyncController syncController;
    private OtaController otaController;
    private RetrofitManager retrofitManager;
    private SyncActivityManager syncActivityManager;
    private User   user;
    private UserDatabaseHelper userDatabaseHelper;
    private WorldClockDatabaseHelper worldClockDatabaseHelper;
    private StepsDatabaseHelper stepsDatabaseHelper;
    private SleepDatabaseHelper sleepDatabaseHelper;
    private NotificationDatabaseHelper notificationDatabaseHelper;
    private WatchesDatabaseHelper watchesDatabaseHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        syncController = new SyncControllerImpl(this);
        otaController  = new OtaControllerImpl(this);
        retrofitManager = new RetrofitManager(this);
        syncActivityManager = new SyncActivityManager(this);
        userDatabaseHelper = new UserDatabaseHelper(this);
        worldClockDatabaseHelper = new WorldClockDatabaseHelper(this);
        stepsDatabaseHelper = new StepsDatabaseHelper(this);
        sleepDatabaseHelper = new SleepDatabaseHelper(this);
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
        EventBus.getDefault().register(this);
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

    @Subscribe
    public void onEvent(LittleSyncEvent event) {
        CreateSteps steps = new CreateSteps();
        steps.setUid(Integer.parseInt(getUser().getUserID()));
        steps.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        steps.setSteps(event.getSteps());
        getSyncActivityManager().launchSyncDailySteps(steps);
    }

}
