package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class FacebookMessengerNotification extends NotificationModel {

    public FacebookMessengerNotification(boolean enableNotification)
    {
        super(enableNotification);
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_facebook_messenger_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.facebook_messenger;
    }
}
