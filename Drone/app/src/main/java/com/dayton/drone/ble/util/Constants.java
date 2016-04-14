package com.dayton.drone.ble.util;

/**
 * Created by med on 16/4/14.
 */
public class Constants {

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

}
