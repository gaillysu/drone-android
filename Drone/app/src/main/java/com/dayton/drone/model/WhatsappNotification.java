package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class WhatsappNotification extends NotificationModel {
    public WhatsappNotification(boolean enableNotification) {
        super(enableNotification);
    }

    @Override
    public int getNameStringResource() {
        return R.string.notification_whatsapp_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.whatsapp_messager;
    }
}
