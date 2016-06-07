package com.dayton.drone.ble.model.ancs;

/**
 * Created by med on 16/6/7.
 */
public class Call extends NotificationDataSource{
    private int callType; //incoming call:0 or missed call:1
    public Call(int callType)
    {
        this.callType = callType;
    }
    @Override
    public int getAlertCategory() {
        return callType==0? 0xF0:0xF1;
    }

}
