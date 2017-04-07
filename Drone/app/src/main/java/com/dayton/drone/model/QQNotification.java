package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class QQNotification extends NotificationModel {
    public QQNotification(boolean enableNotification) {
        super(enableNotification);
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_qq_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.qq_notification;
    }
}
