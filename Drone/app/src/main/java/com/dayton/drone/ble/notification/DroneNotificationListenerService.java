package com.dayton.drone.ble.notification;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.ble.datasource.GattAttributesDataSourceImpl;
import com.dayton.drone.ble.model.request.notification.DroneNotificationRequest;

import net.medcorp.library.ble.controller.ConnectionController;
import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.model.request.BLERequestData;
import net.medcorp.library.ble.util.Optional;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Date;

/**
 * Created by med on 16/4/25.
 *
 * NOTICE: this service will run on background, so you must put it in the manifest file and enable it
 * it will auto start when received a system notification
 */
public class DroneNotificationListenerService extends NotificationListenerService  {
    private final String TAG = DroneNotificationListenerService.class.getName();
    private final static int TIME_BETWEEN_TWO_NOTIFS = 5000;
    private static long lastNotificationTimeStamps = 0;
    private static Optional<BLERequestData> pendingBLERequestData = new Optional<>();

    private ApplicationModel application;

    private TelephonyManager telephonyManager;
    private CallStateListener callStateListener;

    @Override
    public void onCreate() {
        super.onCreate();
        application = (ApplicationModel) getApplication();
        //here add a listener to phone manager, some phone models can't catch the incoming call notification from the system statusbar,eg: samung S4
        telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        callStateListener = new CallStateListener();
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        EventBus.getDefault().register(this);
        getNotificationAccessPermission(application);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        telephonyManager.listen(callStateListener, PhoneStateListener.LISTEN_NONE);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(BLEConnectionStateChangedEvent stateChangedEvent) {
        if(stateChangedEvent.isConnected())
        {
            if(pendingBLERequestData.notEmpty())
            {
                ConnectionController.Singleton.getInstance(DroneNotificationListenerService.this, new GattAttributesDataSourceImpl(DroneNotificationListenerService.this))
                        .sendRequest(pendingBLERequestData.get());
                pendingBLERequestData.set(null);
            }
        }
        else
        {
            //discard it when got disconnection
            if(pendingBLERequestData.notEmpty()) {
                pendingBLERequestData.set(null);
            }
        }
    }
    private void sendNotificationRequest()
    {
        if((new Date().getTime() - lastNotificationTimeStamps)< TIME_BETWEEN_TWO_NOTIFS){
            return;
        }
        lastNotificationTimeStamps = new Date().getTime();
        if(ConnectionController.Singleton.getInstance(this, new GattAttributesDataSourceImpl(this)).inOTAMode()){
            return;
        }

        DroneNotificationRequest droneNotificationRequest = new DroneNotificationRequest(application);
        if(application.getSyncController().isConnected())
        {
            ConnectionController.Singleton.getInstance(DroneNotificationListenerService.this, new GattAttributesDataSourceImpl(DroneNotificationListenerService.this))
                    .sendRequest(droneNotificationRequest);
        }
        else {
            //pending this request until reconnect success, and send it
            pendingBLERequestData.set(droneNotificationRequest);
            application.getSyncController().startConnect(false);
        }
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        if(sbn == null) {
            return;
        }
        Notification notification = sbn.getNotification();
        Log.i(TAG, "onNotificationPosted: " + sbn.getPackageName());
        //TODO here use filter (package name or contacts name) to send watch notification request
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }

    private static void getNotificationAccessPermission(final Context ctx) {
        ContentResolver contentResolver = ctx.getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = ctx.getPackageName();

        if (enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName))
        {
            // Let's ask the user to enable notifications
            new AlertDialog.Builder(ctx).setTitle("Notification Access").setMessage("Do you want to enable notifications for Drone?")
                    .setNegativeButton(android.R.string.no, null).setPositiveButton(android.R.string.yes, new AlertDialog.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                    ctx.startActivity(intent);

                }

            }).show();
        }
    }

    private class CallStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING:
                    Log.i(TAG, "onCallStateChanged:" + incomingNumber);
                    break;
            }
        }
    }
}
