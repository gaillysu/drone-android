package com.dayton.drone.bean.menu;

import com.dayton.drone.R;
import com.dayton.drone.activity.ActivitiesActivity;

/**
 * Created by Jason on 2017/5/2.
 */

public class ActivitiesMenuItem implements HomeMenuItem {

    @Override
    public int getMenuItemIndex() {
        return 0;
    }

    @Override
    public int getMenuItemName() {
        return R.string.home_menu_name_activities;
    }

    @Override
    public int getMenuItemIcon() {
        return R.drawable.ic_activities_icon;
    }


    @Override
    public Class getActivityClass() {
        return ActivitiesActivity.class;
    }
}
