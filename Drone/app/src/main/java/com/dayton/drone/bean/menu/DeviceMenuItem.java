package com.dayton.drone.bean.menu;

import com.dayton.drone.R;
import com.dayton.drone.activity.AddWatchActivity;

/**
 * Created by Jason on 2017/5/2.
 */

public class DeviceMenuItem implements HomeMenuItem {

    @Override
    public int getMenuItemIndex() {
        return 7;
    }

    @Override
    public int getMenuItemName() {
        return R.string.home_menu_name_device;
    }

    @Override
    public int getMenuItemIcon() {
        return R.drawable.ic_main_menu_device;
    }

    @Override
    public Class getActivityClass() {
        return AddWatchActivity.class;
    }
}
