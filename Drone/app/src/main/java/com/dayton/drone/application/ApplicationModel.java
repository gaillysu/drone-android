package com.dayton.drone.application;

import android.app.Application;
import android.content.Context;

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
import net.medcorp.library.android.notificationsdk.config.ConfigEditor;
import net.medcorp.library.android.notificationsdk.config.ConfigHelper;
import net.medcorp.library.android.notificationsdk.config.mode.FilterMode;
import net.medcorp.library.android.notificationsdk.config.mode.OverrideMode;
import net.medcorp.library.android.notificationsdk.config.type.FilterType;
import net.medcorp.library.android.notificationsdk.config.type.OverrideType;
import net.medcorp.library.ble.util.Optional;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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
        initializeNotifications();
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


    private void initializeNotifications(){
        final ConfigEditor configEditor = new ConfigEditor((Context)this);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final Iterator<String> iterator = ConfigHelper.getCallPackages((Context)this).iterator();
        while (iterator.hasNext()) {
            hashMap.put(iterator.next(), Integer.toString(2));
        }
        final Iterator<String> iterator2 = ConfigHelper.getSMSPackages((Context)this).iterator();
        while (iterator2.hasNext()) {
            hashMap.put(iterator2.next(), Integer.toString(6));
        }
        final Iterator<String> iterator3 = ConfigHelper.getMessengerPackages((Context)this).iterator();
        while (iterator3.hasNext()) {
            hashMap.put(iterator3.next(), Integer.toString(6));
        }
        final Iterator<String> iterator4 = ConfigHelper.getEmailPackages((Context)this).iterator();
        while (iterator4.hasNext()) {
            hashMap.put(iterator4.next(), Integer.toString(3));
        }
        final Iterator<String> iterator5 = ConfigHelper.getSocialPackages((Context)this).iterator();
        while (iterator5.hasNext()) {
            hashMap.put(iterator5.next(), Integer.toString(11));
        }
        hashMap.put("ans_incoming_call", Integer.toString(240));
        hashMap.put("ans_missed_call", Integer.toString(241));
        hashMap.put("ans_sms", Integer.toString(242));
        configEditor.setOverrideMode(OverrideType.CATEGORY, OverrideMode.STRICT);
        configEditor.setOverrideMap(OverrideType.CATEGORY, hashMap);
        configEditor.setOverrideFallback(OverrideType.CATEGORY, Integer.toString(255));
        final HashSet<String> set = new HashSet<String>();
        set.addAll(ConfigHelper.getCallPackages((Context)this));
        configEditor.setFilterSet(FilterType.PACKAGE, set);
        configEditor.setFilterMode(FilterType.PACKAGE, FilterMode.BLACKLIST);
        configEditor.apply();
    }
}
