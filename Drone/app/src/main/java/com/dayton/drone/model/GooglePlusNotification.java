package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class GooglePlusNotification extends NotificationModel {
    public GooglePlusNotification(boolean enableNotification) {
        super(enableNotification);
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_googleplus_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.facebook_notification;
    }
}
