package com.dayton.drone.event;

/**
 * Created by med on 16/4/12.
 */
public class CloudSyncEvent {

    public CloudSyncEvent(CLOUD_SYNC_EVENT status) {
        this.status = status;
    }

    public enum CLOUD_SYNC_EVENT{
        STARTED,
        STOPPED
    }

    final CLOUD_SYNC_EVENT status;

    public CLOUD_SYNC_EVENT getStatus() {
        return status;
    }
}
