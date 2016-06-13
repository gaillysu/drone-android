package com.dayton.drone.ble.util;

/**
 * Created by med on 16/4/14.
 */
public class Constants {

    public final static int MTU = 20;

    public enum SystemStatus {
        LowMemory(0),
        InvalidTime(3),
        GoalCompleted(4),
        ActivityDataAvailable(5),
        SubscribedToNotifications(7),
        SystemReset(8);
        private int status;
        private SystemStatus(int status) {this.status = status;}
        public  int rawValue() {return status;}
    }

    public enum SystemEvent{
         GoalCompleted(1),
         LowMemory(2),
         ActivityDataAvailable(3),
         BatteryStatusChanged(5);
        private SystemEvent(int event) {
            this.event = event;
        }
        final private int event;
        public  int rawValue() {return event;}
    }

    public enum ActivityDataStatus {
         EmptyData(0),MoreData(1);
        final int status;
        ActivityDataStatus(int status) {
            this.status = status;
        }
        public  int rawValue() {return status;}
    }

    public enum SystemConfigID{
        DNDConfig(1),
        AirplaneMode(2),
        Enabled(4),
        ClockFormat(8),
        SleepConfig(9);
        private SystemConfigID(int id) {
            this.id = id;
        }
        final private int id;
        public  int rawValue() {return id;}
    }

    public enum ApplicationID {
        WorldClock(1),ActivityTracking(2);
        final int id;
        private ApplicationID(int id) {
            this.id = id;
        }
        public  int rawValue() {return id;}
    }

    public enum BatteryStatus {
        InUse(0),Charging(1),Damaged(2),Calculating(3);
        final int status;
        private BatteryStatus(int status) {
            this.status = status;
        }
        public  int rawValue() {return status;}
    }

    //below used by android notification server
    public enum NotificationCommand {
        ReadAttributes(0x01),TriggerAction(0x03),ReadExtendAttributes(0x05);
        final int command;
        private NotificationCommand(int command) {
            this.command = command;
        }
        public  byte rawValue() {return (byte) command;}
    }

    public enum AttributeCode {
        Category(0x01),ApplicationPackage(0x02),Number(0x03),Priority(0x04)
        ,Visibility(0x05),Title(0x06),Subtitle(0x07),Text(0x08),When(0x09),ApplicationName(0x0A);
        final int value;
        private AttributeCode(int value) {
            this.value = value;
        }
        public  byte rawValue() {return (byte)value;}
    }

}
