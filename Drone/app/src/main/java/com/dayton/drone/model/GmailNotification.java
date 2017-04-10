package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class GmailNotification extends NotificationModel {

    public GmailNotification(boolean enableNotification)
    {
        super(enableNotification);
    }
    @Override
    public int getNameStringResource() {
        return R.string.notification_gmail_inbox_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.gmail_inbox_tools;
    }
}
