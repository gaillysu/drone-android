package com.dayton.drone.ble.controller;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.ble.datasource.GattAttributesDataSourceImpl;
import com.dayton.drone.ble.model.TimeZoneModel;
import com.dayton.drone.ble.model.WeatherLocationModel;
import com.dayton.drone.ble.model.WeatherUpdateModel;
import com.dayton.drone.ble.model.packet.ActivityPacket;
import com.dayton.drone.ble.model.packet.GetBatteryPacket;
import com.dayton.drone.ble.model.packet.GetStepsGoalPacket;
import com.dayton.drone.ble.model.packet.SystemEventPacket;
import com.dayton.drone.ble.model.packet.SystemStatusPacket;
import com.dayton.drone.ble.model.packet.base.DronePacket;
import com.dayton.drone.ble.model.request.SetWeatherLocationsRequest;
import com.dayton.drone.ble.model.request.UpdateWeatherInfomationRequest;
import com.dayton.drone.ble.model.request.battery.GetBatteryRequest;
import com.dayton.drone.ble.model.request.clean.ForgetWatchRequest;
import com.dayton.drone.ble.model.request.dfu.SetDFUModeRequest;
import com.dayton.drone.ble.model.request.init.GetSystemStatus;
import com.dayton.drone.ble.model.request.init.SetAppConfigRequest;
import com.dayton.drone.ble.model.request.init.SetRTCRequest;
import com.dayton.drone.ble.model.request.init.SetSystemConfig;
import com.dayton.drone.ble.model.request.setting.SetGoalRequest;
import com.dayton.drone.ble.model.request.setting.SetStepsToWatchReuqest;
import com.dayton.drone.ble.model.request.setting.SetUserProfileRequest;
import com.dayton.drone.ble.model.request.sync.GetActivityRequest;
import com.dayton.drone.ble.model.request.sync.GetStepsGoalRequest;
import com.dayton.drone.ble.model.request.worldclock.SetWorldClockRequest;
import com.dayton.drone.ble.notification.ListenerService;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.ble.util.WeatherID2Code;
import com.dayton.drone.event.BatteryStatusChangedEvent;
import com.dayton.drone.event.BigSyncEvent;
import com.dayton.drone.event.CityNumberChangedEvent;
import com.dayton.drone.event.CityWeatherChangedEvent;
import com.dayton.drone.event.DownloadStepsEvent;
import com.dayton.drone.event.GoalCompletedEvent;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.event.LowMemoryEvent;
import com.dayton.drone.event.ProfileChangedEvent;
import com.dayton.drone.event.StepsGoalChangedEvent;
import com.dayton.drone.event.Timer10sEvent;
import com.dayton.drone.event.WorldClockChangedEvent;
import com.dayton.drone.model.DailySteps;
import com.dayton.drone.model.Steps;
import com.dayton.drone.network.request.GetWeatherRequest;
import com.dayton.drone.network.response.model.GetWeatherModel;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.Common;
import com.dayton.drone.utils.SpUtils;
import com.dayton.drone.utils.StepsHandler;
import com.dayton.drone.utils.WeatherUtils;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import net.medcorp.library.ble.controller.ConnectionController;
import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.event.BLEResponseDataEvent;
import net.medcorp.library.ble.model.request.BLERequestData;
import net.medcorp.library.ble.model.response.BLEResponseData;
import net.medcorp.library.ble.model.response.MEDRawData;
import net.medcorp.library.ble.util.Optional;
import net.medcorp.library.ble.util.QueuedMainThreadHandler;
import net.medcorp.library.worldclock.City;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.dayton.drone.ble.util.Constants.ApplicationID.WorldClock;

/**
 * Created by med on 16/4/12.
 */
public class SyncControllerImpl implements  SyncController{

    private final static String TAG = SyncController.class.getSimpleName();
    private final ApplicationModel application;
    private ConnectionController connectionController;
    private List<MEDRawData> packetsBuffer = new ArrayList<MEDRawData>();

    private Timer autoSyncTimer = null;
    private Optional<Date> theBigSyncStartDate = new Optional<>();
    private int baseSteps = 0;
    private final long BIG_SYNC_INTERVAL = 5*60* 1000L; //5minutes
    private boolean bigSyncFirst = true;
    private final Object lockObject = new Object();
    /**
     * Wait for given number of milliseconds.
     * @param millis waiting period
     */
    protected void waitFor(final int millis) {
        synchronized (lockObject) {
            try {
                lockObject.wait(millis);
            } catch (final InterruptedException e) {
                Log.e(TAG,"sleep interrupted");
            }
        }
    }

    private void startTimer(final boolean autoSync) {
        if(autoSyncTimer!=null)autoSyncTimer.cancel();
        autoSyncTimer = new Timer();
        long LITTLE_SYNC_INTERVAL = 10000L;
        autoSyncTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(autoSync)
                {
                    //send little sync request
                    sendRequest(new GetStepsGoalRequest(application));
                    //send big sync request

                    if ((new Date().getTime() - SpUtils.getLongMethod(application, CacheConstants.LAST_BIG_SYNC_TIMESTAMP, new Date().getTime())) > BIG_SYNC_INTERVAL || bigSyncFirst) {
                        bigSyncFirst = false;
                    }
                }
                EventBus.getDefault().post(new Timer10sEvent());
            }
        }, LITTLE_SYNC_INTERVAL,LITTLE_SYNC_INTERVAL);
    }

    public  SyncControllerImpl(ApplicationModel application){
        this.application = application;
        if (!isToday(SpUtils.getLongMethod(application,CacheConstants.TODAY_DATE,0))){
            Log.w("Karl","Resetting today!");
            SpUtils.putLongMethod(application,CacheConstants.TODAY_DATE,new Date().getTime());
            SpUtils.putIntMethod(application,CacheConstants.TODAY_BASESTEP,0);
            SpUtils.putBoolean(application,CacheConstants.TODAY_RESET,false);
            SpUtils.putIntMethod(application,CacheConstants.TODAY_STEP,0);
        }

        connectionController = ConnectionController.Singleton.getInstance(application,new GattAttributesDataSourceImpl(application));
        EventBus.getDefault().register(this);
        ServiceConnection serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.v(TAG, name + " Service disconnected");
                localBinder = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.v(TAG, name + " Service connected");
                localBinder = (LocalService.LocalBinder) service;
            }
        };
        application.getApplicationContext().bindService(new Intent(application, LocalService.class), serviceConnection, Activity.BIND_AUTO_CREATE);
        startTimer(false);
    }

    @Override
    public void startConnect(boolean forceScan) {
        if(forceScan){
            connectionController.forgetSavedAddress();
        }

        connectionController.scan();
    }

    @Override
    public boolean isConnected() {
        return connectionController.isConnected();
    }

    @Override
    public void disConnect() {
        connectionController.disconnect();
    }

    @Override
    public String getFirmwareVersion() {
        return connectionController.getBluetoothVersion();
    }

    @Override
    public String getSoftwareVersion() {
        return connectionController.getSoftwareVersion();
    }

    @Override
    public void forgetDevice() {
        //steps0:clean pair infomation on watch and drop connection
        sendRequest(new ForgetWatchRequest(application));
        waitFor(200);
        //step1:disconnect
        if(connectionController.isConnected())
        {
            connectionController.disconnect();
        }
        //step2:unpair this watch from system bluetooth setting
        connectionController.unPairDevice(connectionController.getSaveAddress());
        //step3:reset MAC address and firstly run flag and big sync stamp
        connectionController.forgetSavedAddress();

    }

    @Override
    public void findDevice() {

    }

    @Override
    public void getBattery() {
        sendRequest(new GetBatteryRequest(application));
    }

    @Override
    public void startNotificationListener() {
        //force android NotificationManagerService to rebind user NotificationListenerService
        // http://www.zhihu.com/question/33540416/answer/113706620
        PackageManager pm = application.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(application, ListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(application, ListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

    }

    /**
     * send request  package to watch by using a queue
     * @param request
     */
    private void sendRequest(final BLERequestData request) {
        if(connectionController.inOTAMode()) {
            return;
        }
        if(!isConnected()) {
            return;
        }
        QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, request.getClass().getName());
                connectionController.sendRequest(request);
            }
        });
    }

    private void setWorldClock(List<City> worldClockList)
    {
        List<TimeZoneModel> timeZoneModelList = new ArrayList<>();
        for(City city:worldClockList)
        {
            timeZoneModelList.add(new TimeZoneModel(city.getOffSetFromGMT()/60,city.getName()));
        }
        sendRequest(new SetWorldClockRequest(application,timeZoneModelList));
    }
    @Subscribe
    public void onEvent(BLEResponseDataEvent eventData)
    {
        BLEResponseData data = eventData.getData();
        if(data.getType().equals(MEDRawData.TYPE))
        {
            final MEDRawData droneData = (MEDRawData) data;

            if(droneData.getRawData().length==1 && (byte)0xFF == droneData.getRawData()[0])
            {
                return;
            }
            packetsBuffer.add(droneData);
            if((byte)0x80 == droneData.getRawData()[0])
            {
                DronePacket packet = new DronePacket(packetsBuffer);
                //if packets invaild, discard them, and reset buffer
                if(!packet.isVaildPackets())
                {
                    packetsBuffer.clear();
                    QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).next();
                    return;
                }
                if(GetSystemStatus.HEADER == packet.getHeader())
                {

                    SystemStatusPacket systemStatusPacket = packet.newSystemStatusPacket();
                    Log.i(TAG,"GetSystemStatus return status value: " + systemStatusPacket.getStatus());


                    Date date = new Date(SpUtils.getLongMethod(application, CacheConstants.TODAY_DATE, 0));
                    Log.w("Karl","getting today. The date is = " + date.getTime());
                    Log.w("Karl","checking if saved date = today and if today reset is necessary");
                    if(SpUtils.getBoolean(application,CacheConstants.TODAY_RESET,false) && Common.removeTimeFromDate(date).getTime() == Common.removeTimeFromDate(new Date()).getTime())
                    {
                        Log.w("Karl","Apparently it is today and we reset today!");
                        baseSteps = SpUtils.getIntMethod(application, CacheConstants.TODAY_BASESTEP, 0);
                    } else {
                        Log.w("Karl","Apparently it isn't");
                        baseSteps = 0;
                    }
                    if((systemStatusPacket.getStatus() & Constants.SystemStatus.SystemReset.rawValue())== Constants.SystemStatus.SystemReset.rawValue())
                    {
                        SpUtils.printAllConstants(application);
                        SpUtils.putBoolean(application,CacheConstants.TODAY_RESET,true);
                        sendRequest(new SetSystemConfig(application,1,0, 0, 0, Constants.SystemConfigID.ClockFormat));
                        sendRequest(new SetSystemConfig(application,1,0, 0, 0, Constants.SystemConfigID.Enabled));
                        sendRequest(new SetSystemConfig(application,1,0, 0, 0, Constants.SystemConfigID.SleepConfig));
                        if(getFirmwareVersion()!=null&&Float.valueOf(getFirmwareVersion())>=0.04f) {
                            sendRequest(new SetSystemConfig(application,1,0, 0, 0, Constants.SystemConfigID.CompassAutoOnDuration));
                            sendRequest(new SetSystemConfig(application,1,0, 0, 0, Constants.SystemConfigID.TopKeyCustomization));
                        }
                        sendRequest(new SetRTCRequest(application));
                        sendRequest(new SetAppConfigRequest(application, WorldClock));
                        sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.ActivityTracking));
                        if(getFirmwareVersion()!=null&&Float.valueOf(getFirmwareVersion())>=0.04f) {
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Weather));
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Compass));
                        }
                        sendRequest(new SetUserProfileRequest(application,application.getUser()));
                        //set goal to watch
                        sendRequest(new SetGoalRequest(application, SpUtils.getIntMethod(application, CacheConstants.GOAL_STEP, 10000)));
                        //if the cached date is today,use the cached steps to set watch
                        //set world clock to watch

                        setWorldClock(application.getSelectedCities());


                        if(SpUtils.getBoolean(application, CacheConstants.TODAY_RESET,false)){
                            List<Optional<Steps>> steps = application.getStepsDatabaseHelper().get(application.getUser().getUserID(),new Date());
                            if (!steps.isEmpty()){
                                if(!steps.get(0).isEmpty()){
                                    baseSteps = steps.get(0).get().getDailySteps();
                                }
                            }else{
                                baseSteps = 0;
                            }
                            sendRequest(new SetStepsToWatchReuqest(application,baseSteps));
                            Log.w("Karl","Must Sync steps = false");
                            SpUtils.putBoolean(application, CacheConstants.MUST_SYNC_STEPS,false);
                        }
                        Log.w("Karl","Setting the base step to = " + baseSteps);
                        SpUtils.putIntMethod(application, CacheConstants.TODAY_BASESTEP, baseSteps);
                        SpUtils.printAllConstants(application);
                    }
                    else if((systemStatusPacket.getStatus() & Constants.SystemStatus.InvalidTime.rawValue())==Constants.SystemStatus.InvalidTime.rawValue())
                    {
                        sendRequest(new SetRTCRequest(application));
                        sendRequest(new SetAppConfigRequest(application, WorldClock));
                        sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.ActivityTracking));
                        if(getFirmwareVersion()!=null&&Float.valueOf(getFirmwareVersion())>=0.04f) {
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Weather));
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Compass));
                        }
                        sendRequest(new SetUserProfileRequest(application,application.getUser()));
                    }
                    if((systemStatusPacket.getStatus() & Constants.SystemStatus.GoalCompleted.rawValue())==Constants.SystemStatus.GoalCompleted.rawValue())
                    {
                        EventBus.getDefault().post(new GoalCompletedEvent());
                        sendRequest(new SetGoalRequest(application,SetGoalRequest.DEFAULTSTEPSGOAL));
                    }
                    if((systemStatusPacket.getStatus() & Constants.SystemStatus.ActivityDataAvailable.rawValue())==Constants.SystemStatus.ActivityDataAvailable.rawValue())
                    {
                        theBigSyncStartDate = new Optional<>(new Date());
                        EventBus.getDefault().post(new BigSyncEvent(theBigSyncStartDate.get(), BigSyncEvent.BIG_SYNC_EVENT.STARTED));
                        sendRequest(new GetActivityRequest(application));
                    }
                    if((systemStatusPacket.getStatus() & Constants.SystemStatus.SubscribedToNotifications.rawValue())==Constants.SystemStatus.SubscribedToNotifications.rawValue())
                    {
                        Log.d(TAG,"Subscribed to notifications success.");
                    }
                    //here sync weather
                    updateCitiesWeather();
                    //here start ANCS service
                    application.getApplicationContext().bindService(new Intent(application, ListenerService.class), notificationServiceConnection, Activity.BIND_AUTO_CREATE);
                     //here start little sync timer
                    startTimer(true);
                }
                else if(GetActivityRequest.HEADER == packet.getHeader())
                {
                    ActivityPacket activityPacket = packet.newActivityPacket();
                    Log.i(TAG,activityPacket.getDate().toString() + " time frame steps: " + activityPacket.getSteps());
                    Steps steps = new Steps(activityPacket.getSteps(), activityPacket.getDate().getTime());
                    steps.setDate((Common.removeTimeFromDate(new Date(activityPacket.getDate().getTime()))).getTime());
                    steps.setUserID(application.getUser().getUserID());
                    steps.setStepsGoal(SpUtils.getIntMethod(application, CacheConstants.GOAL_STEP, 10000));
                    application.getStepsDatabaseHelper().update(steps);
                    //save the oldest activity date as colud sync starting date
                    if(theBigSyncStartDate.isEmpty() || (theBigSyncStartDate.notEmpty() && theBigSyncStartDate.get().getTime()>steps.getDate()))
                    {
                        theBigSyncStartDate.set(new Date(steps.getDate()));
                    }
                    if(activityPacket.getMore()==Constants.ActivityDataStatus.MoreData.rawValue())
                    {
                        sendRequest(new GetActivityRequest(application));
                    }
                    else
                    {
                        EventBus.getDefault().post(new BigSyncEvent(theBigSyncStartDate.get(), BigSyncEvent.BIG_SYNC_EVENT.STOPPED));
                        SpUtils.putLongMethod(application, CacheConstants.LAST_BIG_SYNC_TIMESTAMP, new Date().getTime());
                        StepsHandler stepsHandler = new StepsHandler(application.getStepsDatabaseHelper(), application.getUser());
                        DailySteps dailySteps= stepsHandler.getDailySteps(new Date());
                        Log.w("Karl","Set today steps =" + dailySteps.getDailySteps());
                        SpUtils.printAllConstants(application);
                    }
                }
                else if(GetStepsGoalRequest.HEADER == packet.getHeader())
                {
                    GetStepsGoalPacket getStepsGoalPacket = packet.newGetStepsGoalPacket();
                    int steps = getStepsGoalPacket.getSteps();
                    int goal  = getStepsGoalPacket.getGoal();
                    Log.i(TAG,"steps: " + steps + ",goal: " + goal + ",baseSteps: " + baseSteps);
                    SpUtils.putIntMethod(application, CacheConstants.GOAL_STEP, goal);
                    if (!isToday(SpUtils.getLongMethod(application,CacheConstants.TODAY_DATE,0))) {
                        baseSteps = 0;
                    }
                    SpUtils.putLongMethod(application, CacheConstants.TODAY_DATE, new Date().getTime());
                    SpUtils.putIntMethod(application, CacheConstants.TODAY_STEP, steps);

                    EventBus.getDefault().post(new LittleSyncEvent(steps, goal));
                }
                else if(GetBatteryRequest.HEADER == packet.getHeader())
                {
                    GetBatteryPacket getBatteryPacket = packet.newGetBatteryPacket();
                    EventBus.getDefault().post(new BatteryStatusChangedEvent(getBatteryPacket.getBatteryStatus(), getBatteryPacket.getBatteryLevel()));
                }
                //system event: 0x02, this is sent by watch proactively,pls refer to Constants.SystemEvent
                else if(SystemEventPacket.HEADER == packet.getHeader())
                {
                    SystemEventPacket systemEventPacket = packet.newSystemEventPacket();
                    Log.i(TAG,"SystemEventPacket return event value: " + systemEventPacket.getEvent());

                    if(systemEventPacket.getEvent() == Constants.SystemEvent.BatteryStatusChanged.rawValue()) {
                        getBattery();
                    }
                    else if(systemEventPacket.getEvent() == Constants.SystemEvent.GoalCompleted.rawValue()) {
                        sendRequest(new GetActivityRequest(application));
                        EventBus.getDefault().post(new GoalCompletedEvent());
                    }
                    else if(systemEventPacket.getEvent() == Constants.SystemEvent.LowMemory.rawValue()) {
                        EventBus.getDefault().post(new LowMemoryEvent());
                    }
                    else if(systemEventPacket.getEvent() == Constants.SystemEvent.ActivityDataAvailable.rawValue()) {
                        theBigSyncStartDate = new Optional<>(new Date());
                        EventBus.getDefault().post(new BigSyncEvent(theBigSyncStartDate.get(), BigSyncEvent.BIG_SYNC_EVENT.STARTED));
                        sendRequest(new GetActivityRequest(application));
                    }
                }
                //process done current cmd's response, request next cmd
                packetsBuffer.clear();
                QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).next();
            }
        }
    }



    @Subscribe
    public void onEvent(BLEConnectionStateChangedEvent stateChangedEvent) {
        if(stateChangedEvent.isConnected()) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    packetsBuffer.clear();
                    //reset last big sync timestamp every got connected.
                    Log.w("Karl","Last time big sync=" + new Date().getTime());
                    SpUtils.putLongMethod(application, CacheConstants.LAST_BIG_SYNC_TIMESTAMP, new Date().getTime());
                    SpUtils.putLongMethod(application, CacheConstants.LAST_CONNECTED_TIMESTAMP, new Date().getTime());
                    sendRequest(new GetSystemStatus(application));
                }
            }, 2000);
        } else {
            QueuedMainThreadHandler.getInstance(QueuedMainThreadHandler.QueueType.SyncController).clear();
            packetsBuffer.clear();
        }
    }

    @Subscribe
    public void onEvent(WorldClockChangedEvent worldClockChangedEvent) {
        setWorldClock(application.getSelectedCities());
    }
    @Subscribe
    public void onEvent(StepsGoalChangedEvent stepsGoalChangedEvent) {
        sendRequest(new SetGoalRequest(application,stepsGoalChangedEvent.getStepsGoal()));
    }
    @Subscribe
    public void onEvent(ProfileChangedEvent profileChangedEvent) {
        sendRequest(new SetUserProfileRequest(application,profileChangedEvent.getUser()));
    }

    /**
     * new feature for firmware r4+
    1: when user select/unselect one city, after save done,send event 'CityNumberChangedEvent', syncControllerImpl will catch it and call this function
    2: call this function every hour after connected
    3: call this function every connected
     */
    private void updateCitiesWeather()
    {
        if(getFirmwareVersion()!=null&&Float.valueOf(getFirmwareVersion())>=0.04f) {
            final Set<String> cities = WeatherUtils.getWeatherCities(application);
            for (final String name : cities) {
                final GetWeatherRequest request = new GetWeatherRequest(name, application.getRetrofitManager().getWeatherApiKey());
                application.getRetrofitManager().requestWeather(request, new RequestListener<GetWeatherModel>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        Log.e("weather", "failed by " + spiceException.getCause().getMessage());
                    }

                    @Override
                    public void onRequestSuccess(GetWeatherModel getWeatherModel) {
                        Log.e(getWeatherModel.getName(), "" + new Gson().toJson(getWeatherModel));
                        EventBus.getDefault().post(new CityWeatherChangedEvent(name, getWeatherModel));
                    }
                });
            }
        }
    }

    @Subscribe
    public void onEvent(Timer10sEvent timer10sEvent) {
        if ((new Date().getTime() - SpUtils.getLongMethod(application, CacheConstants.LAST_CONNECTED_TIMESTAMP, new Date().getTime())) >= 60*60*1000l) {
            SpUtils.putLongMethod(application, CacheConstants.LAST_CONNECTED_TIMESTAMP, new Date().getTime());
            updateCitiesWeather();
        }
    }
    @Subscribe
    public void onEvent(CityNumberChangedEvent cityNumberChangedEvent) {
        int index = 0;
        Set<String> cities = WeatherUtils.getWeatherCities(application);
        List<WeatherLocationModel> weatherLocationModelList = new ArrayList<>();
        for(String city:cities){
            weatherLocationModelList.add(new WeatherLocationModel((byte) (index), (byte) city.length(), city));
            index++;
        }
        SetWeatherLocationsRequest setWeatherLocations = new SetWeatherLocationsRequest(application,weatherLocationModelList);
        sendRequest(setWeatherLocations);
        updateCitiesWeather();
    }

    @Subscribe
    public void onEvent(CityWeatherChangedEvent cityWeatherChangedEvent) {
        int locationId = WeatherUtils.getWeatherLocationId(application,cityWeatherChangedEvent.getName());
        WeatherUpdateModel[] entries = {
                new WeatherUpdateModel((byte)(locationId), (short) ((short)cityWeatherChangedEvent.getWeatherModel().getMain().getTemp()-273), WeatherID2Code.getWeatherCodeByID(cityWeatherChangedEvent.getWeatherModel().getWeather()[0].getId(),true)),
        };
        Log.i(TAG,"No. "+(locationId) + " city changed: "  + cityWeatherChangedEvent.getName() + ",temp: "+(cityWeatherChangedEvent.getWeatherModel().getMain().getTemp() -273) + ",weather: " + cityWeatherChangedEvent.getWeatherModel().getWeather()[0].getMain());
        sendRequest(new UpdateWeatherInfomationRequest(application,Arrays.asList(entries)));
    }

    @Subscribe
    public void onEvent(final DownloadStepsEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (event.getStatus() == DownloadStepsEvent.DOWNLOAD_STEPS_EVENT.STOPPED) {
                    StepsHandler stepsHandler = new StepsHandler(application.getStepsDatabaseHelper(), application.getUser());
                    DailySteps dailySteps= stepsHandler.getDailySteps(new Date());
                    if(dailySteps.getDailySteps()>SpUtils.getIntMethod(application, CacheConstants.TODAY_STEP, 0))
                    {
                        Log.w("Karl"," Syncing 3 things =");
                        Log.w("Karl"," Syncing basestep and step things =" + baseSteps);
                        Log.w("Karl"," Syncing date=" + new Date().getTime());
                        baseSteps = dailySteps.getDailySteps();
                        SpUtils.putLongMethod(application, CacheConstants.TODAY_DATE, new Date().getTime());
                        SpUtils.putIntMethod(application, CacheConstants.TODAY_BASESTEP, baseSteps);
                        SpUtils.putIntMethod(application,CacheConstants.TODAY_STEP,baseSteps);
                    }
                    SpUtils.printAllConstants(application);
                }
            }
        });
    }
    //local service
    static public class LocalService extends Service
    {
        BroadcastReceiver myReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
                {
                    Log.i("LocalService","Screen On");
                    if(!ConnectionController.Singleton.getInstance(context, new GattAttributesDataSourceImpl(context)).isConnected())
                    {
                        ConnectionController.Singleton.getInstance(context,new GattAttributesDataSourceImpl(context)).scan();
                    }
                }
            }
        };

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return new LocalBinder();
        }

        @Override
        public void onCreate() {
            super.onCreate();
            registerReceiver(myReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            unregisterReceiver(myReceiver);
        }

        public class LocalBinder extends Binder {
            //you can add some functions here
        }

    }
    private LocalService.LocalBinder localBinder = null;

    private ServiceConnection notificationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(TAG, name+" Service disconnected");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.v(TAG, name+" Service connected");
            startNotificationListener();
        }
    };

    private boolean isToday (long timestamp){
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTimeInMillis(timestamp);

        if(now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)) {
            if(now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

}
