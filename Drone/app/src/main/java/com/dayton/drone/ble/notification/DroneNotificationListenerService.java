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
import com.dayton.drone.ble.model.ancs.Call;
import com.dayton.drone.ble.model.ancs.Email;
import com.dayton.drone.ble.model.ancs.Facebook;
import com.dayton.drone.ble.model.ancs.NotificationDataSource;
import com.dayton.drone.ble.model.ancs.Sms;
import com.dayton.drone.ble.model.ancs.Wechat;
import com.dayton.drone.ble.model.ancs.Whatsapp;
import com.dayton.drone.ble.model.request.notification.DroneNotificationTrigger;

import net.medcorp.library.ble.controller.ConnectionController;
import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
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
    final private String TAG = DroneNotificationListenerService.class.getName();
    final static int TIME_BETWEEN_TWO_NOTIFS = 5000;
    static long lastNotificationTimeStamps = 0;
    static Optional<DroneNotificationTrigger> pendingDroneNotificationTrigger = new Optional<>();

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
            if(pendingDroneNotificationTrigger.notEmpty())
            {
                pendingDroneNotificationTrigger.get().doNotificationAlert();
            }
        }
        else
        {
            //discard it when got disconnection
            if(pendingDroneNotificationTrigger.notEmpty()) {
                pendingDroneNotificationTrigger.set(null);
            }
        }
    }
    private void sendNotification(NotificationDataSource datasource)
    {
        if((new Date().getTime() - lastNotificationTimeStamps)< TIME_BETWEEN_TWO_NOTIFS){
            return;
        }
        lastNotificationTimeStamps = new Date().getTime();
        if(ConnectionController.Singleton.getInstance(this, new GattAttributesDataSourceImpl(this)).inOTAMode()){
            return;
        }
        DroneNotificationTrigger droneNotificationTrigger = new DroneNotificationTrigger(application.getSyncController().getGattServerService(),datasource);
        if(application.getSyncController().isConnected())
        {
            droneNotificationTrigger.doNotificationAlert();
        }
        else {
            //pending this request until reconnect success, and send it
            pendingDroneNotificationTrigger.set(droneNotificationTrigger);
            application.getSyncController().startConnect(false);
        }
    }
    @Override
    public void onNotificationPosted(StatusBarNotification statusBarNotification) {
        if(statusBarNotification == null) {
            return;
        }
        Notification notification = statusBarNotification.getNotification();
        Log.v(TAG, "got system Notification : "+statusBarNotification);
        if (notification != null)
        {
            if(statusBarNotification.getPackageName().equals("com.android.incallui") && statusBarNotification.getTag().contains("incall"))
            {
                sendNotification(new Call(true,statusBarNotification.getId()));
            }
            else if(statusBarNotification.getPackageName().equals("com.android.server.telecom") && statusBarNotification.getTag().contains("missed_call"))
            {
                sendNotification(new Call(false,statusBarNotification.getId()));
            }
            else if(statusBarNotification.getPackageName().equals("com.google.android.talk")
                    || statusBarNotification.getPackageName().equals("com.android.mms")
                    || statusBarNotification.getPackageName().equals("com.google.android.apps.messaging")
                    || statusBarNotification.getPackageName().equals("com.sonyericsson.conversations")
                    || statusBarNotification.getPackageName().equals("com.htc.sense.mms")
                    || statusBarNotification.getPackageName().equals("com.google.android.talk")
                    ) {
                //BLE keep-connect service will process this message
                //if(helper.getState(new SmsNotification()).isOn())
                    sendNotification(new Sms(statusBarNotification.getId()));
            } else if(statusBarNotification.getPackageName().equals("com.android.email")
                    || statusBarNotification.getPackageName().equals("com.google.android.email")
                    || statusBarNotification.getPackageName().equals("com.google.android.gm")
                    || statusBarNotification.getPackageName().equals("com.kingsoft.email")
                    || statusBarNotification.getPackageName().equals("com.tencent.androidqqmail")
                    || statusBarNotification.getPackageName().equals("com.outlook.Z7")){
                //if(helper.getState(new EmailNotification()).isOn())
                    sendNotification(new Email(statusBarNotification.getId()));
            }
            else if(statusBarNotification.getPackageName().equals("com.facebook.katana")){
                //if(helper.getState(new FacebookNotification()).isOn())
                    sendNotification(new Facebook(statusBarNotification.getId()));
            } else if(statusBarNotification.getPackageName().equals("com.tencent.mm")){
                //if(helper.getState(new WeChatNotification()).isOn())
                    sendNotification(new Wechat(statusBarNotification.getId()));
            } else if(statusBarNotification.getPackageName().equals("com.whatsapp")){
                //if(helper.getState(new WhatsappNotification()).isOn())
                    sendNotification(new Whatsapp(statusBarNotification.getId()));
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }


    /**
     * popup a dialog to let user enable the app access system notification
     * @param ctx
     */
    public static void getNotificationAccessPermission(final Context ctx) {
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
                    //sendNotification(new Call(0));
                    break;
            }
        }
    }
}
