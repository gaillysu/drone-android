package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class SnapchatNotification extends NotificationModel {
    public SnapchatNotification(boolean enableNotification) {
        super(enableNotification);
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_snapchat_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.facebook_notification;
    }
}
