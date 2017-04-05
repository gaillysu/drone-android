package com.dayton.drone.application;

import android.app.Application;
import android.content.Context;

import com.dayton.drone.R;
import com.dayton.drone.ble.controller.SyncController;
import com.dayton.drone.ble.controller.SyncControllerImpl;
import com.dayton.drone.ble.notification.PackageFilterHelper;
import com.dayton.drone.ble.util.NotificationPermission;
import com.dayton.drone.cloud.SyncActivityManager;
import com.dayton.drone.database.DroneDatabaseModule;
import com.dayton.drone.database.entry.NotificationDatabaseHelper;
import com.dayton.drone.database.entry.StepsDatabaseHelper;
import com.dayton.drone.database.entry.UserDatabaseHelper;
import com.dayton.drone.database.entry.WatchesDatabaseHelper;
import com.dayton.drone.event.NotificationPackagesChangedEvent;
import com.dayton.drone.model.Contact;
import com.dayton.drone.model.Notification;
import com.dayton.drone.model.User;
import com.dayton.drone.network.RetrofitManager;
import com.google.gson.Gson;

import net.medcorp.library.android.notificationsdk.config.ConfigEditor;
import net.medcorp.library.android.notificationsdk.config.ConfigHelper;
import net.medcorp.library.android.notificationsdk.config.mode.FilterMode;
import net.medcorp.library.android.notificationsdk.config.mode.OverrideMode;
import net.medcorp.library.android.notificationsdk.config.type.FilterType;
import net.medcorp.library.android.notificationsdk.config.type.OverrideType;
import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.util.Optional;
import net.medcorp.library.worldclock.City;
import net.medcorp.library.worldclock.WorldClockDatabaseHelper;
import net.medcorp.library.worldclock.WorldClockLibraryModule;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by karl-john on 18/3/16.
 */
public class ApplicationModel extends Application {
    private SyncController syncController;
    private RetrofitManager retrofitManager;
    private SyncActivityManager syncActivityManager;
    private User user;
    private UserDatabaseHelper userDatabaseHelper;
    private StepsDatabaseHelper stepsDatabaseHelper;
    private NotificationDatabaseHelper notificationDatabaseHelper;
    private WatchesDatabaseHelper watchesDatabaseHelper;
    private WorldClockDatabaseHelper worldClockDatabaseHelper;

    @Override
    public void onCreate(){
        super.onCreate();
        Realm.init(this);
        worldClockDatabaseHelper = new WorldClockDatabaseHelper(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().name("drone")
                .modules(new DroneDatabaseModule(),new WorldClockLibraryModule()).build());

        //EventBus.builder().sendNoSubscriberEvent(false).logNoSubscriberMessages(false).installDefaultEventBus();
        syncController = new SyncControllerImpl(this);
        retrofitManager = new RetrofitManager(this);
        syncActivityManager = new SyncActivityManager(this);
        userDatabaseHelper = new UserDatabaseHelper(this);
        stepsDatabaseHelper = new StepsDatabaseHelper(this);
        notificationDatabaseHelper = new NotificationDatabaseHelper(this);
        watchesDatabaseHelper = new WatchesDatabaseHelper(this);

        Optional<User> loginUser = userDatabaseHelper.getLoginUser();
        if(loginUser.notEmpty()) {
            user = loginUser.get();
        } else {
            user = new User();
            user.setUserID("0");//"0" means anyone user
        }
        initializeNotifications();
        EventBus.getDefault().register(this);
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
    public WorldClockDatabaseHelper getWorldClockDatabaseHelp(){
        return worldClockDatabaseHelper;
    }
    public NotificationDatabaseHelper getNotificationDatabaseHelper(){
        return notificationDatabaseHelper;
    }

    public WatchesDatabaseHelper getWatchesDatabaseHelper(){
        return watchesDatabaseHelper;
    }

    public void initializeNotifications(){
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
        List<Notification> notifications = getNotificationDatabaseHelper().get("unknown");
        List<String> contactsList = new ArrayList<>();
        if(!notifications.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONObject(notifications.get(0).getContactsList()).getJSONArray("contacts");
                for(int i=0;i<jsonArray.length();i++)
                {
                    Contact contact = new Gson().fromJson(jsonArray.getJSONObject(i).toString(),Contact.class);
                    contactsList.add(contact.getName());
                    contactsList.addAll(Arrays.asList(contact.getNumber().split(";")));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        set.addAll(contactsList);
        configEditor.setFilterSet(FilterType.CONTACT, set);
        configEditor.setFilterMode(FilterType.CONTACT, FilterMode.DISABLED);
        //start package filter in whitelist mode
        final HashSet<String> setPackages = new HashSet<String>();
        setPackages.addAll(PackageFilterHelper.getCallPackages(PackageFilterHelper.getCallFilterEnable(this),this,R.array.CALL_APPS));
        setPackages.addAll(PackageFilterHelper.getMessagerPackages(PackageFilterHelper.getMessagerFacebookFilterEnable(this),this,R.array.MESSAGER_FACEBOOK));
        setPackages.addAll(PackageFilterHelper.getSmsPackages(PackageFilterHelper.getSmsFilterEnable(this),this,R.array.SMS_APPS));
        setPackages.addAll(PackageFilterHelper.getEmailPackages(PackageFilterHelper.getEmailFilterEnable(this),this,R.array.EMAIL_APPS));
        setPackages.addAll(PackageFilterHelper.getMessagerPackages(PackageFilterHelper.getMessagerTelegramFilterEnable(this),this,R.array.MESSAGER_TELEGRAM));
        setPackages.addAll(PackageFilterHelper.getCalendarPackages(PackageFilterHelper.getCalendarFilterEnable(this),this,R.array.CALENDAR_APPS));
        setPackages.addAll(PackageFilterHelper.getSocialPackages(PackageFilterHelper.getFacebookFilterEnable(this),this,R.array.SOCIAL_APPS_FACEBOOK));
        setPackages.addAll(PackageFilterHelper.getSocialPackages(PackageFilterHelper.getGooglePlusFilterEnable(this),this,R.array.SOCIAL_APPS_GOOGLE));
        setPackages.addAll(PackageFilterHelper.getSocialPackages(PackageFilterHelper.getInstagramFilterEnable(this),this,R.array.SOCIAL_APPS_INSTAGRAM));
        setPackages.addAll(PackageFilterHelper.getSocialPackages(PackageFilterHelper.getSnapchatFilterEnable(this),this,R.array.SOCIAL_APPS_SNAPCHAT));
        setPackages.addAll(PackageFilterHelper.getSocialPackages(PackageFilterHelper.getLinkedinFilterEnable(this),this,R.array.SOCIAL_APPS_LINKEDIN));
        setPackages.addAll(PackageFilterHelper.getSocialPackages(PackageFilterHelper.getTwitterFilterEnable(this),this,R.array.SOCIAL_APPS_TWITTER));
        setPackages.addAll(PackageFilterHelper.getSocialPackages(PackageFilterHelper.getWechatFilterEnable(this),this,R.array.SOCIAL_APPS_WECHAT));
        setPackages.addAll(PackageFilterHelper.getSocialPackages(PackageFilterHelper.getWhatsappFilterEnable(this),this,R.array.SOCIAL_APPS_WHATSAPP));
        setPackages.addAll(PackageFilterHelper.getSocialPackages(PackageFilterHelper.getQQFilterEnable(this),this,R.array.SOCIAL_APPS_QQ));

        configEditor.setFilterSet(FilterType.PACKAGE, setPackages);
        configEditor.setFilterMode(FilterType.PACKAGE, FilterMode.WHITELIST);
        configEditor.apply();
    }


    @Subscribe
    public void onConnectionStateChanged(BLEConnectionStateChangedEvent event){
        if(event.isConnected()){
            NotificationPermission.getNotificationAccessPermission(this);
        }
    }

    @Subscribe
    public void onEvent(NotificationPackagesChangedEvent event){
        initializeNotifications();
    }

    public List<City> getSelectedCities() {
        RealmResults<City> realmCities = Realm.getDefaultInstance().where(City.class).equalTo("selected",true).findAll();
        List<City> cities = new ArrayList<>();
        for (City city: realmCities) {
            cities.add(city);
        }
        return cities;
    }
}