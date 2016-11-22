package com.dayton.drone.model;


import com.dayton.drone.R;

/**
 * Created by med on 16/11/22.
 */

public class SocialNotification extends NotificationModel {

    public SocialNotification(boolean enableNotification) {
        super(enableNotification);
    }
    @Override
    public int getNameStringResource() {
        return R.string.notification_social_title;
    }

    @Override
    public int getImageResource() {
        //TODO here should replace facebook icon with a new one
        return R.drawable.facebook_notification;
    }
}
