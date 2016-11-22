package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class TelephoneNotification extends NotificationModel {

    @Override
    public boolean getSwitchSign() {
        return false;
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_call_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.call_notification;
    }
}
