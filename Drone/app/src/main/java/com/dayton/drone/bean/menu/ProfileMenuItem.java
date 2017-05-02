package com.dayton.drone.bean.menu;

import com.dayton.drone.R;
import com.dayton.drone.activity.ProfileActivity;

/**
 * Created by Jason on 2017/5/2.
 */

public class ProfileMenuItem implements HomeMenuItem {
    @Override
    public int getMenuItemIndex() {
        return 6;
    }

    @Override
    public int getMenuItemName() {
        return R.string.home_menu_name_profile;
    }

    @Override
    public int getMenuItemIcon() {
        return R.drawable.ic_main_menu_profile;
    }

    @Override
    public Class getActivityClass() {
        return ProfileActivity.class;
    }
}
