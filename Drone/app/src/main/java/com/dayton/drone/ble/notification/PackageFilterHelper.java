package com.dayton.drone.ble.notification;

import android.content.Context;

import com.dayton.drone.utils.SpUtils;

import net.medcorp.library.android.notificationsdk.config.ConfigHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by med on 16/11/21.
 */

public class PackageFilterHelper {
    //default  enable all packages as below list:
    final static Boolean defaultEnable = true;

    final static String CALL_FILTER_ENABLE="call_filter_enable";
    final static String SMS_FILTER_ENABLE = "sms_filter_enable";
    final static String EMAIL_FILTER_ENABLE = "email_filter_enable";
    final static String CALENDAR_FILTER_ENABLE = "calendar_filter_enable";
    final static String SOCIAL_FILTER_ENABLE = "social_filter_enable";

    //NOTICE: BELOW APP LIST IS MOST OF PHONES INSTALLED,PERHAPS THIRD-PARTY APPS ARE NOT INCLUDE,YOU CAN ADD THEM.
    final static Set<String> CALL_APPS = new HashSet<String>(Arrays.asList("com.android.dialer", "com.android.server.telecom", "com.android.providers.telephony", "com.android.incallui", "com.android.phone"));
    final static Set<String> SMS_APPS = new HashSet<String>(Arrays.asList("com.android.mms","com.google.android.talk","com.google.android.apps.messaging","com.sonyericsson.conversations","com.htc.sense.mms"));
    final static Set<String> EMAIL_APPS = new HashSet<String>(Arrays.asList("com.yahoo.mobile.client.android.mail", "com.google.android.gm", "com.google.android.apps.inbox", "com.microsoft.office.outlook","com.android.email","com.google.android.email","com.kingsoft.email","com.tencent.androidqqmail","com.outlook.Z7"));
    final static Set<String> CALENDAR_APPS = new HashSet<String>(Arrays.asList("com.google.android.calendar","com.android.calendar"));
    final static Set<String> SOCIAL_APPS = new HashSet<String>(Arrays.asList("com.facebook.katana", "com.google.android.apps.plus", "com.instagram.android", "com.snapchat.android", "com.linkedin.android", "com.twitter.android", "com.pinterest","com.tencent.mm","com.whatsapp","com.tencent.mobileqq"));


    public static void setCallFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,CALL_FILTER_ENABLE,value);
    }
    public static Boolean getCallFilterEnable(Context context){
        return SpUtils.getBoolean(context,CALL_FILTER_ENABLE,defaultEnable);
    }
    public static void setSmsFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,SMS_FILTER_ENABLE,value);
    }
    public static Boolean getSmsFilterEnable(Context context){
        return SpUtils.getBoolean(context,SMS_FILTER_ENABLE,defaultEnable);
    }
    public static void setEmailFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,EMAIL_FILTER_ENABLE,value);
    }
    public static Boolean getEmailFilterEnable(Context context){
        return SpUtils.getBoolean(context,EMAIL_FILTER_ENABLE,defaultEnable);
    }
    public static void setCalendarFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,CALENDAR_FILTER_ENABLE,value);
    }
    public static Boolean getCalendarFilterEnable(Context context){
        return SpUtils.getBoolean(context,CALENDAR_FILTER_ENABLE,defaultEnable);
    }
    public static void setSocialFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,SOCIAL_FILTER_ENABLE,value);
    }
    public static Boolean getSocialFilterEnable(Context context){
        return SpUtils.getBoolean(context,SOCIAL_FILTER_ENABLE,defaultEnable);
    }

    public static Set<String> getCallPackages(Boolean isEnable) {
        HashSet<String> set = new HashSet<String>();
        if(isEnable){
            set.addAll(CALL_APPS);
            set.addAll(ConfigHelper.getANSCallPackages());
        }
        return set;
    }
    public static Set<String> getSmsPackages(Boolean isEnable) {
        HashSet<String> set = new HashSet<String>();
        if(isEnable){
            set.addAll(SMS_APPS);
            set.addAll(ConfigHelper.getANSSMSPackages());
        }
        return set;
    }
    public static Set<String> getEmailPackages(Boolean isEnable) {
        return isEnable?EMAIL_APPS:new HashSet<String>();
    }
    public static Set<String> getCalendarPackages(Boolean isEnable) {
        return isEnable?CALENDAR_APPS:new HashSet<String>();
    }
    public static Set<String> getSocialPackages(Boolean isEnable) {
        return isEnable?SOCIAL_APPS:new HashSet<String>();
    }
}
