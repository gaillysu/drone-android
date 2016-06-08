package com.dayton.drone.ble.model.ancs;

/**
 * Created by med on 16/6/7.
 */
public abstract class NotificationDataSource {
    private final int notificationID;
    public NotificationDataSource(int notificationID){this.notificationID = notificationID;}
    int getAlertType(){return 1;}
    abstract byte getAlertCategory();
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
        return notificationID;
    }
}
