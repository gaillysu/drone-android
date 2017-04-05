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
    //common message, include android Message and iMessage
    final static String SMS_FILTER_ENABLE = "sms_filter_enable";
    //special messager, include facebook and telegram messager
    final static String MESSAGER_FACEBOOK_FILTER_ENABLE = "messager_facebook_filter_enable";
    final static String MESSAGER_TELEGRAM_FILTER_ENABLE = "messager_telegram_filter_enable";
    final static String EMAIL_FILTER_ENABLE = "email_filter_enable";
    final static String CALENDAR_FILTER_ENABLE = "calendar_filter_enable";

    final static String SOCIAL_Facebook_ENABLE = "facebook_filter_enable";
    final static String SOCIAL_GooglePlus_ENABLE = "googleplus_filter_enable";
    final static String SOCIAL_Instagram_ENABLE = "instagram_filter_enable";
    final static String SOCIAL_Snapchat_ENABLE = "snapchat_filter_enable";
    final static String SOCIAL_Linkedin_ENABLE = "linkedin_filter_enable";
    final static String SOCIAL_Twitter_ENABLE = "twitter_filter_enable";
    final static String SOCIAL_Wechat_ENABLE = "wechat_filter_enable";
    final static String SOCIAL_Whatsapp_ENABLE = "whatsapp_filter_enable";
    final static String SOCIAL_QQ_ENABLE = "whatsapp_QQ_enable";



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

    public static void setMessagerFacebookFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,MESSAGER_FACEBOOK_FILTER_ENABLE,value);
    }
    public static Boolean getMessagerFacebookFilterEnable(Context context){
        return SpUtils.getBoolean(context,MESSAGER_FACEBOOK_FILTER_ENABLE,defaultEnable);
    }

    public static void setMessagerTelegramFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,MESSAGER_TELEGRAM_FILTER_ENABLE,value);
    }
    public static Boolean getMessagerTelegramFilterEnable(Context context){
        return SpUtils.getBoolean(context,MESSAGER_TELEGRAM_FILTER_ENABLE,defaultEnable);
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

    //detail social apps
    public static void setFacebookFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,SOCIAL_Facebook_ENABLE,value);
    }
    public static Boolean getFacebookFilterEnable(Context context){
        return SpUtils.getBoolean(context,SOCIAL_Facebook_ENABLE,defaultEnable);
    }

    public static void setGooglePlusFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,SOCIAL_GooglePlus_ENABLE,value);
    }
    public static Boolean getGooglePlusFilterEnable(Context context){
        return SpUtils.getBoolean(context,SOCIAL_GooglePlus_ENABLE,defaultEnable);
    }

    public static void setInstagramFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,SOCIAL_Instagram_ENABLE,value);
    }
    public static Boolean getInstagramFilterEnable(Context context){
        return SpUtils.getBoolean(context,SOCIAL_Instagram_ENABLE,defaultEnable);
    }

    public static void setSnapchatFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,SOCIAL_Snapchat_ENABLE,value);
    }
    public static Boolean getSnapchatFilterEnable(Context context){
        return SpUtils.getBoolean(context,SOCIAL_Snapchat_ENABLE,defaultEnable);
    }

    public static void setLinkedinFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,SOCIAL_Linkedin_ENABLE,value);
    }
    public static Boolean getLinkedinFilterEnable(Context context){
        return SpUtils.getBoolean(context,SOCIAL_Linkedin_ENABLE,defaultEnable);
    }

    public static void setTwitterFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,SOCIAL_Twitter_ENABLE,value);
    }
    public static Boolean getTwitterFilterEnable(Context context){
        return SpUtils.getBoolean(context,SOCIAL_Twitter_ENABLE,defaultEnable);
    }

    public static void setWechatFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,SOCIAL_Wechat_ENABLE,value);
    }
    public static Boolean getWechatFilterEnable(Context context){
        return SpUtils.getBoolean(context,SOCIAL_Wechat_ENABLE,defaultEnable);
    }

    public static void setWhatsappFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,SOCIAL_Whatsapp_ENABLE,value);
    }
    public static Boolean getWhatsappFilterEnable(Context context){
        return SpUtils.getBoolean(context,SOCIAL_Whatsapp_ENABLE,defaultEnable);
    }

    public static void setQQFilterEnable(Context context,boolean value){
        SpUtils.putBoolean(context,SOCIAL_QQ_ENABLE,value);
    }
    public static Boolean getQQFilterEnable(Context context){
        return SpUtils.getBoolean(context,SOCIAL_QQ_ENABLE,defaultEnable);
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
    public static Set<String> getMessagerPackages(Boolean isEnable,Context context,int resID) {
        return isEnable?initAppList(context,resID):new HashSet<String>();
    }
}
