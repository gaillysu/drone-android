package com.dayton.drone.bean.menu;

import com.dayton.drone.R;
import com.dayton.drone.activity.WorldClockActivity;

/**
 * Created by Jason on 2017/5/2.
 */

public class CityNavigationMenuItem implements HomeMenuItem {
    @Override
    public int getMenuItemIndex() {
        return 2;
    }

    @Override
    public int getMenuItemName() {
        return R.string.home_menu_name_city_navigation;
    }

    @Override
    public int getMenuItemIcon() {
        return R.drawable.ic_main_menu_city_map;
    }

    @Override
    public Class getActivityClass() {
        return WorldClockActivity.class;
    }
}
