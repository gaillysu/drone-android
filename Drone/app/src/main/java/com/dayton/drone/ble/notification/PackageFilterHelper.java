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

    private static HashSet<String> initAppList(Context context,int resID)
    {
        HashSet<String> set = new HashSet<String>();
        set.addAll(Arrays.asList(context.getResources().getStringArray(resID)));
        return set;
    }

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

    public static Set<String> getCallPackages(Boolean isEnable,Context context,int resID) {
        HashSet<String> set = new HashSet<String>();
        if(isEnable){
            set.addAll(initAppList(context,resID));
            set.addAll(ConfigHelper.getANSCallPackages());
        }
        return set;
    }
    public static Set<String> getSmsPackages(Boolean isEnable,Context context,int resID) {
        HashSet<String> set = new HashSet<String>();
        if(isEnable){
            set.addAll(initAppList(context,resID));
            set.addAll(ConfigHelper.getANSSMSPackages());
        }
        return set;
    }
    public static Set<String> getEmailPackages(Boolean isEnable,Context context,int resID) {
        return isEnable?initAppList(context,resID):new HashSet<String>();
    }
    public static Set<String> getCalendarPackages(Boolean isEnable,Context context,int resID) {
        return isEnable?initAppList(context,resID):new HashSet<String>();
    }
    public static Set<String> getSocialPackages(Boolean isEnable,Context context,int resID) {
        return isEnable?initAppList(context,resID):new HashSet<String>();
    }
}
