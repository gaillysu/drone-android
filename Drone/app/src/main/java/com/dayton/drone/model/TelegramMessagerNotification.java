package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class TelegramMessagerNotification extends NotificationModel {

    public TelegramMessagerNotification(boolean enableNotification)
    {
        super(enableNotification);
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_telegram_messager_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.telegram_messager;
    }
}
