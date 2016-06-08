package com.dayton.drone.ble.model.ancs;

/**
 * Created by med on 16/6/7.
 */
public class Call extends NotificationDataSource{
    private boolean incall; //incoming call:true or missed call:false
    public Call(boolean incall,int notificationID)
    {
        super(notificationID);
        this.incall = incall;
    }
    @Override
    public byte getAlertCategory() {
        return incall? (byte)(0xF0):(byte)(0xF1);
    }

}
