package com.dayton.drone.model;

/**
 * Created by Jason on 2016/11/21.
 */

public abstract class NotificationModel   {
    boolean enableNotification;
    public NotificationModel(boolean enableNotification)
    {
        this.enableNotification = enableNotification;
    }
    public boolean getSwitchSign(){
        return enableNotification;
    }
    public abstract int getNameStringResource();
    public abstract int getImageResource();
}
