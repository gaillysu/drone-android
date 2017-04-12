package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class OutlookNotification extends NotificationModel {

    public OutlookNotification(boolean enableNotification)
    {
        super(enableNotification);
    }
    @Override
    public int getNameStringResource() {
        return R.string.notification_outlook_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.outlook_microsoft;
    }
}