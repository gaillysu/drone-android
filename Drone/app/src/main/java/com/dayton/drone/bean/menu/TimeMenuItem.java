package com.dayton.drone.bean.menu;

import com.dayton.drone.R;
import com.dayton.drone.activity.WorldClockActivity;

/**
 * Created by Jason on 2017/5/2.
 */

public class TimeMenuItem implements HomeMenuItem {
    @Override
    public int getMenuItemIndex() {
        return 1;
    }

    @Override
    public int getMenuItemName() {
        return R.string.home_menu_name_time;
    }

    @Override
    public int getMenuItemIcon() {
        return R.drawable.ic_main_menu_time;
    }

    @Override
    public Class getActivityClass() {
        return WorldClockActivity.class;
    }
}
