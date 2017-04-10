package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class MessageNotification extends NotificationModel {

    public MessageNotification(boolean enableNotification)
    {
        super(enableNotification);
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_message_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.google_talk;
    }
}
