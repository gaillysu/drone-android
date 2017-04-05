package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class FacebookMessagerNotification extends NotificationModel {

    public FacebookMessagerNotification(boolean enableNotification)
    {
        super(enableNotification);
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_facebook_messager_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.message_notification;
    }
}
