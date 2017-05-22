package com.dayton.drone.bean.menu;

import com.dayton.drone.R;
import com.dayton.drone.activity.NewSetNotificationActivity;

/**
 * Created by Jason on 2017/5/2.
 */

public class NotificationMenuItem implements HomeMenuItem {
    @Override
    public int getMenuItemIndex() {
        return 5;
    }

    @Override
    public int getMenuItemName() {
        return R.string.home_menu_name_notification;
    }

    @Override
    public int getMenuItemIcon() {
        return R.drawable.ic_main_menu_notifications;
    }

    @Override
    public Class getActivityClass() {
        return NewSetNotificationActivity.class;
    }
}
