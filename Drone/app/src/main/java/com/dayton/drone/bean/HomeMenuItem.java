package com.dayton.drone.bean;

/**
 * Created by Jason on 2017/4/28.
 */

public class HomeMenuItem {
    private int menuItemIndex;
    private String describe;
    private Class activityClass;

    public HomeMenuItem(int menuItemIndex, String describe, Class activityClass) {
        this.menuItemIndex = menuItemIndex;
        this.describe = describe;
        this.activityClass = activityClass;
    }

    public int getMenuItemIndex() {
        return menuItemIndex;
    }

    public void setMenuItemIndex(int menuItemIndex) {
        this.menuItemIndex = menuItemIndex;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Class getActivityClass() {
        return activityClass;
    }

    public void setActivityClass(Class activityClass) {
        this.activityClass = activityClass;
    }
}
