package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class CalendarNotification extends NotificationModel {
    public CalendarNotification(boolean enableNotification) {
        super(enableNotification);
    }
    @Override
    public int getNameStringResource() {
        return R.string.notification_calendar_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.calendar_tools;
    }
}
