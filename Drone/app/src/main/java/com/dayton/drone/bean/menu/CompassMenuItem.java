package com.dayton.drone.bean.menu;

import com.dayton.drone.R;
import com.dayton.drone.activity.WorldClockActivity;

/**
 * Created by Jason on 2017/5/2.
 */

public class CompassMenuItem implements HomeMenuItem{
    @Override
    public int getMenuItemIndex() {
        return 3;
    }

    @Override
    public int getMenuItemName() {
        return R.string.home_menu_name_compass;
    }

    @Override
    public int getMenuItemIcon() {
        return R.drawable.ic_main_menu_navigation;
    }

    @Override
    public Class getActivityClass() {
        return WorldClockActivity.class;
    }
}
