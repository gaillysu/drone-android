package com.dayton.drone.model;

import com.dayton.drone.R;

/**
 * Created by Jason on 2016/11/21.
 */

public class WeChatNotification extends NotificationModel {

    public WeChatNotification(boolean enableNotification) {
        super(enableNotification);
    }
    @Override
    public int getNameStringResource() {
        return R.string.notification_wechat_title;
    }

    @Override
    public int getImageResource() {
        return R.drawable.wechat_messenger;
    }
}
