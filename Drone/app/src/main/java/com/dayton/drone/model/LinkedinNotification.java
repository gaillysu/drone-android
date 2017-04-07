package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class LinkedinNotification extends NotificationModel {
    public LinkedinNotification(boolean enableNotification) {
        super(enableNotification);
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_linkedin_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.linkedin_messager;
    }
}
