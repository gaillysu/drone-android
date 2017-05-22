package com.dayton.drone.bean.menu;

import com.dayton.drone.R;
import com.dayton.drone.activity.HotKeyActivity;

/**
 * Created by Jason on 2017/5/2.
 */

public class HotKeyMenuItem implements HomeMenuItem {
    @Override
    public int getMenuItemIndex() {
        return 4;
    }

    @Override
    public int getMenuItemName() {
        return R.string.home_menu_name_hot_key;
    }

    @Override
    public int getMenuItemIcon() {
        return R.drawable.ic_main_menu_hot_key;
    }

    @Override
    public Class getActivityClass() {
        return HotKeyActivity.class;
    }
}
