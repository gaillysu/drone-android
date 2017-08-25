package com.dayton.drone.ble.controller;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;

import com.dayton.drone.R;
import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.ble.datasource.GattAttributesDataSourceImpl;
import com.dayton.drone.ble.model.DailyAlarmModel;
import com.dayton.drone.ble.model.TimeZoneModel;
import com.dayton.drone.ble.model.WeatherLocationModel;
import com.dayton.drone.ble.model.WeatherUpdateModel;
import com.dayton.drone.ble.model.packet.ActivityPacket;
import com.dayton.drone.ble.model.packet.FindPhonePacket;
import com.dayton.drone.ble.model.packet.GetBatteryPacket;
import com.dayton.drone.ble.model.packet.GetStepsGoalPacket;
import com.dayton.drone.ble.model.packet.SystemEventPacket;
import com.dayton.drone.ble.model.packet.SystemStatusPacket;
import com.dayton.drone.ble.model.packet.base.DronePacket;
import com.dayton.drone.ble.model.request.DisableUrbanNavigation;
import com.dayton.drone.ble.model.request.EnableUrbanNavigation;
import com.dayton.drone.ble.model.request.SetCountdownTimerRequest;
import com.dayton.drone.ble.model.request.SetDailyAlarmRequest;
import com.dayton.drone.ble.model.request.SetWeatherLocationsRequest;
import com.dayton.drone.ble.model.request.StartSystemSettingRequest;
import com.dayton.drone.ble.model.request.UpdateUrbanNavigation;
import com.dayton.drone.ble.model.request.UpdateWeatherInfomationRequest;
import com.dayton.drone.ble.model.request.battery.GetBatteryRequest;
import com.dayton.drone.ble.model.request.clean.ForgetWatchRequest;
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
import com.dayton.drone.ble.util.WeatherIcon;
import com.dayton.drone.database.bean.HourlyForecastBean;
import com.dayton.drone.event.BatteryStatusChangedEvent;
import com.dayton.drone.event.BigSyncEvent;
import com.dayton.drone.event.CityForecastChangedEvent;
import com.dayton.drone.event.CityNumberChangedEvent;
import com.dayton.drone.event.DownloadStepsEvent;
import com.dayton.drone.event.GoalCompletedEvent;
import com.dayton.drone.event.LittleSyncEvent;
import com.dayton.drone.event.LowMemoryEvent;
import com.dayton.drone.event.ProfileChangedEvent;
import com.dayton.drone.event.RunForegroundEvent;
import com.dayton.drone.event.StepsGoalChangedEvent;
import com.dayton.drone.event.Timer10sEvent;
import com.dayton.drone.event.WorldClockChangedEvent;
import com.dayton.drone.model.DailySteps;
import com.dayton.drone.model.Steps;
import com.dayton.drone.network.request.GetForecastRequest;
import com.dayton.drone.network.response.model.GetForecastModel;
import com.dayton.drone.shell.Shell;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.Common;
import com.dayton.drone.utils.SoundPlayer;
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
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
    private boolean isHoldRequest = false;

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

    @Override
    public void calibrateWatch(int operation) {
        sendRequest(new StartSystemSettingRequest(application,StartSystemSettingRequest.StartSystemSettingID.AnalogMovement.rawValue(),operation));
    }

    @Override
    public void enableCompass(boolean enable) {
        sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Compass,enable?(byte)1:(byte)0));
    }

    @Override
    public void setCompassAutoOnDuration(int duration) {
        sendRequest(new SetSystemConfig(application, (short) duration,Constants.SystemConfigID.CompassAutoOnDuration));
    }

    @Override
    public void startNavigation(double latitude, double longitude, String address) {
        sendRequest(new EnableUrbanNavigation(application,(long)(latitude*10000000),(long)(longitude*10000000),address));
    }

    @Override
    public void stopNavigation() {
        sendRequest(new DisableUrbanNavigation(application));
    }

    @Override
    public void updateNavigation(double latitude, double longitude,long distanceInMeters) {
        sendRequest(new UpdateUrbanNavigation(application,(long)(latitude*10000000),(long)(longitude*10000000),distanceInMeters));
    }

    @Override
    public void setHotKeyFunction(int functionId) {
        sendRequest(new SetSystemConfig(application, (byte)functionId, Constants.SystemConfigID.TopKeyCustomization));
    }

    @Override
    public void setCompassTimeout(int timeoutInseconds) {
        sendRequest(new SetSystemConfig(application, (byte)timeoutInseconds, Constants.SystemConfigID.CompassTimeout));
    }

    @Override
    public void calibrateCompass(int operation) {
        sendRequest(new StartSystemSettingRequest(application,StartSystemSettingRequest.StartSystemSettingID.Compass.rawValue(),operation));
    }

    @Override
    public void setClockFormat(boolean format24Hour) {
        sendRequest(new SetSystemConfig(application,format24Hour?(byte)1:0, Constants.SystemConfigID.ClockFormat));
    }

    @Override
    public void setCountdownTimer(int countdownInMinutes) {
        sendRequest(new SetCountdownTimerRequest(application,(short)countdownInMinutes));
    }

    @Override
    public void setDailyAlarm(List<DailyAlarmModel> dailyAlarmModels) {
        sendRequest(new SetDailyAlarmRequest(application,dailyAlarmModels));
    }

    @Override
    public void setHoldRequest(boolean holdRequest) {
        isHoldRequest = holdRequest;
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
        //when OTA mode, disable syncController send any request command to BLE
        if (isHoldRequest) {
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
            //getOffSetFromGMT() unit is "minutes", pls make sure "worldclock" database table "TimeZone" is right
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
                        sendRequest(new SetSystemConfig(application,SpUtils.get24HourFormat(application)?(byte)1:0, Constants.SystemConfigID.ClockFormat));
                        sendRequest(new SetSystemConfig(application,Constants.SystemConfigID.Enabled));
                        sendRequest(new SetSystemConfig(application,0, 0, 0, Constants.SystemConfigID.SleepConfig));
                        if(getFirmwareVersion()!=null&&Float.valueOf(getFirmwareVersion())>=0.04f) {
                            sendRequest(new SetSystemConfig(application, (short) SpUtils.getIntMethod(application,CacheConstants.COMPASS_AUTO_ON_DURATION,CacheConstants.COMPASS_AUTO_ON_DURATION_DEFAULT),Constants.SystemConfigID.CompassAutoOnDuration));
                        }
                        sendRequest(new SetRTCRequest(application));
                        sendRequest(new SetAppConfigRequest(application, WorldClock, (byte)1));
                        sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.ActivityTracking, (byte)1));
                        if(getFirmwareVersion()!=null&&Float.valueOf(getFirmwareVersion())>=0.04f) {
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Weather, (byte)1));
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Compass, SpUtils.getBoolean(application,CacheConstants.ENABLE_COMPASS,true)?(byte)1:(byte)0));
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Timer, (byte)1));
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Stopwatch, (byte)1));
                        }
                        sendRequest(new SetUserProfileRequest(application,application.getUser()));
                        //set goal to watch
                        sendRequest(new SetGoalRequest(application, SpUtils.getIntMethod(application, CacheConstants.GOAL_STEP, 10000)));

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
                        sendRequest(new SetAppConfigRequest(application, WorldClock, (byte)1));
                        sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.ActivityTracking, (byte)1));
                        if(getFirmwareVersion()!=null&&Float.valueOf(getFirmwareVersion())>=0.04f) {
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Weather, (byte)1));
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Compass, SpUtils.getBoolean(application,CacheConstants.ENABLE_COMPASS,true)?(byte)1:(byte)0));
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Timer, (byte)1));
                            sendRequest(new SetAppConfigRequest(application, Constants.ApplicationID.Stopwatch, (byte)1));
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
                    if((systemStatusPacket.getStatus() & Constants.SystemStatus.WeatherDataNeeded.rawValue())==Constants.SystemStatus.WeatherDataNeeded.rawValue())
                    {
                        initWeatherLocation();
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
                    else if(systemEventPacket.getEvent() == Constants.SystemEvent.WeatherDataExpired.rawValue()) {
                        updateCitiesWeather();
                    }
                }
                else if (FindPhonePacket.HEADER == packet.getHeader()) {
                    FindPhonePacket findPhonePacket = new FindPhonePacket(packet.getPackets());
                    if (localBinder != null) {
                        if(findPhonePacket.getFindPhoneOperation()==1) {
                            localBinder.findCellPhone();
                        }
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
        setWorldClock(application.getWorldClockDatabaseHelper().getSelect());
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
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(getFirmwareVersion()!=null&&Float.valueOf(getFirmwareVersion())>=0.04f)
                {
                    City localCity = new City();
                    localCity.setName(WeatherUtils.getLocalCityName());
                    Location location = WeatherUtils.getLocalCityLocation(application);
                    if(location!=null) {
                        localCity.setLat(location.getLatitude());
                        localCity.setLng(location.getLongitude());
                    }
                    else {
                        List<City> allCities = application.getWorldClockDatabaseHelper().getAll();
                        for(City city:allCities)
                        {
                            if(city.getName().equals(localCity.getName()))
                            {
                                localCity.setLat(city.getLat());
                                localCity.setLng(city.getLng());
                                break;
                            }
                        }
                    }
                    final List<City> cities = application.getWorldClockDatabaseHelper().getSelect();
                    cities.add(0,localCity);
                    for (final City city : cities)
                    {
                        long currentStampInSeconds = new DateTime().getMillis()/1000;
                        List<HourlyForecastBean> hourlyForecastBeanList = application.getCityWeatherDatabaseHelper().get(city.getName());
                        boolean found = false;
                        if(hourlyForecastBeanList.size()>0)
                        {
                            if(currentStampInSeconds>=hourlyForecastBeanList.get(0).getTime() && currentStampInSeconds<=hourlyForecastBeanList.get(hourlyForecastBeanList.size()-1).getTime())
                            {
                                found = true;
                            }
                        }
                        //from network
                        if(!found) {
                            final GetForecastRequest request = new GetForecastRequest(city.getLat() + "," + city.getLng(), application.getRetrofitManager().getWeatherApiKey());
                            application.getRetrofitManager().requestWeather(request, new RequestListener<GetForecastModel>() {
                                @Override
                                public void onRequestFailure(SpiceException spiceException) {
                                    if(spiceException.getCause() != null) {
                                        Log.e("weather", "failed by " + spiceException.getCause().getMessage());
                                    }
                                }

                                @Override
                                public void onRequestSuccess(GetForecastModel getForecastModel) {
                                    Log.i(city.getName(), "" + new Gson().toJson(getForecastModel));
                                    if (getForecastModel.getHourly().getData().length > 0) {
                                        float temp = getForecastModel.getHourly().getData()[0].getTemperature();
                                        String icon = getForecastModel.getHourly().getData()[0].getIcon();
                                        int locationId = cities.indexOf(city);
                                        EventBus.getDefault().post(new CityForecastChangedEvent(city.getName(), temp, icon, locationId));
                                        application.getCityWeatherDatabaseHelper().addOrUpdate(city.getName(), getForecastModel.getHourly().getData());
                                    }
                                }
                            });
                        }
                        //from cache
                        else {
                            int index = (int) ((currentStampInSeconds-hourlyForecastBeanList.get(0).getTime())/3600);
                            EventBus.getDefault().post(new CityForecastChangedEvent(city.getName(), hourlyForecastBeanList.get(index).getTemperature(), hourlyForecastBeanList.get(index).getIcon(), cities.indexOf(city)));
                        }
                    }
                }
            }
        });
    }

    /**
     * when city number added/removed, or receive status 'ON', invoke this function
     */
    private void initWeatherLocation()
    {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                List<City> select = application.getWorldClockDatabaseHelper().getSelect();
                List<String> cities = new ArrayList<>();
                cities.add(WeatherUtils.getLocalCityName());
                for(City city:select) {
                    cities.add(city.getName());
                }
                List<WeatherLocationModel> weatherLocationModelList = new ArrayList<>();
                for(String city:cities){
                    weatherLocationModelList.add(new WeatherLocationModel((byte) (index), (byte) city.length(), city));
                    index++;
                }
                SetWeatherLocationsRequest setWeatherLocations = new SetWeatherLocationsRequest(application,weatherLocationModelList);
                sendRequest(setWeatherLocations);
            }
        });
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
        initWeatherLocation();
        updateCitiesWeather();
    }

    @Subscribe
    public void onEvent(CityForecastChangedEvent cityForecastChangedEvent) {
        WeatherUpdateModel[] entries = {
                new WeatherUpdateModel(cityForecastChangedEvent.getLocationId(), (cityForecastChangedEvent.getTemp()), new WeatherIcon(cityForecastChangedEvent.getIcon()).convertIcon2Code()),
        };
        Log.i(TAG,"No. "+(cityForecastChangedEvent.getLocationId()) + " city forecast changed: "  + cityForecastChangedEvent.getName() + ",temp: "+(cityForecastChangedEvent.getTemp()) + ",icon: " + cityForecastChangedEvent.getIcon());
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
    @Subscribe
    public void onEvent(RunForegroundEvent event) {
        localBinder.resetRunningForeground();
    }
    //local service
    static public class LocalService extends Service
    {
        boolean runningForeground = false;
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

        private void findCellPhone() {
            Vibrator vibrator = (Vibrator) LocalService.this.getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {1, 2000, 1000, 2000, 1000, 2000, 1000};
            if (vibrator.hasVibrator()) {
                vibrator.cancel();
            }
            vibrator.vibrate(pattern, -1);

            PowerManager pm = (PowerManager) LocalService.this.getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire();
            wl.release();

            //play ring bell to alert user that phone is here
            new SoundPlayer(this).startPlayer(R.raw.bell);
        }

        /**
         *
         * @param keyCode: KeyEvent.KEYCODE_VOLUME_DOWN,KeyEvent.KEYCODE_VOLUME_UP
         */
        void sendHardKey(int keyCode) {
            //if(Shell.isSuAvailable())
            //{
            //    Shell.runCommand(Shell.KEY_INPUT_COMMAND + keyCode);
            //}
            //else
            {
                int action = KeyEvent.ACTION_DOWN;
                if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
                {
                    action = KeyEvent.ACTION_UP;
                    //TODO audioManager.dispatchMediaKeyEvent can't work fine to take photo
                    CameraManager cameraManager = (CameraManager) getApplicationContext().getSystemService(Context.CAMERA_SERVICE);
                    //perhaps cameraManager hide some functions
                }
                AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                if(keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
                {
                    if(audioManager.isMusicActive()) {
                        keyCode = KeyEvent.KEYCODE_MEDIA_PAUSE;
                    }
                    else {
                        keyCode = KeyEvent.KEYCODE_MEDIA_PLAY;
                    }
                }
                audioManager.dispatchMediaKeyEvent(new KeyEvent(action, keyCode));
            }
        }

        private void takePhoto() {
            new ToneGenerator(AudioManager.STREAM_MUSIC,ToneGenerator.MAX_VOLUME).startTone(ToneGenerator.TONE_PROP_BEEP);
            sendHardKey(KeyEvent.KEYCODE_VOLUME_DOWN);
        }

        private void playPauseMusic() {
            sendHardKey(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
        }

        private void nextMusic() {
            sendHardKey(KeyEvent.KEYCODE_MEDIA_NEXT);
        }
        private void preMusic() {
            sendHardKey(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
        }

        public class LocalBinder extends Binder {
            //you can add some functions here
            public void findCellPhone() {
                //LocalService.this.findCellPhone();
                //TODO test code, due to remote camera and music control has no any packets sent from watch when press the hot key
                // case 1: remote camera
                if(!runningForeground) {
                    startActivity(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    runningForeground = true;
                }
                else {
                    takePhoto();
                }

                //case 2: music control
                //playPauseMusic();

                //case 3: next music
                //nextMusic();

                //case 4: previous music
                //preMusic();
            }
            public void takePhoto() {
                LocalService.this.takePhoto();
            }

            public void playPauseMusic() {
                LocalService.this.playPauseMusic();
            }

            public void nextMusic() {
                LocalService.this.nextMusic();
            }
            public void preMusic() {
                LocalService.this.preMusic();
            }
            public void resetRunningForeground() {
                runningForeground = false;
            }
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
