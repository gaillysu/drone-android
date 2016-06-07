package com.dayton.drone.ble.model.ancs;

/**
 * Created by med on 16/6/7.
 */
public abstract class NotificationDataSource {
    private static int notificationID = 0;
    int getAlertType(){return 1;}
    abstract int getAlertCategory();
    int getPriority(){return 3;}
    public byte[] getPayLoad(){
        int notificationID = getNotificationID();
        return new byte[]{(byte)getAlertType(),
                (byte) (notificationID&0xFF),
                (byte) ((notificationID>>8)&0xFF),
                (byte) ((notificationID>>16)&0xFF),
                (byte) ((notificationID>>24)&0xFF),
                (byte) getAlertCategory(),
                0,0,0,0,
                (byte)getPriority(),1,0
        };
    }
    int getNotificationID(){
        notificationID++;
        return notificationID;
    }
}
