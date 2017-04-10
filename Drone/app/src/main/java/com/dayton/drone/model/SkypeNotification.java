package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class SkypeNotification extends NotificationModel {
    public SkypeNotification(boolean enableNotification) {
        super(enableNotification);
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_skype_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.skype_messenger;
    }
}
