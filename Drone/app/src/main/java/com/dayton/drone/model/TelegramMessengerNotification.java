package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class TelegramMessengerNotification extends NotificationModel {

    public TelegramMessengerNotification(boolean enableNotification)
    {
        super(enableNotification);
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_telegram_messenger_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.telegram_messenger;
    }
}
