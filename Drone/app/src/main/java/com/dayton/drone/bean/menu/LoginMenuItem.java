package com.dayton.drone.bean.menu;

import com.dayton.drone.R;
import com.dayton.drone.activity.tutorial.LoginActivity;

/**
 * Created by Jason on 2017/5/2.
 */

public class LoginMenuItem implements HomeMenuItem  {
    @Override
    public int getMenuItemIndex() {
        return 6;
    }

    @Override
    public int getMenuItemName() {
        return R.string.home_menu_name_login;
    }

    @Override
    public int getMenuItemIcon() {
        return R.drawable.ic_main_menu_login;
    }

    @Override
    public Class getActivityClass() {
        return LoginActivity.class;
    }
}
