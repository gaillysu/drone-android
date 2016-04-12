package com.dayton.drone.event;

/**
 * this event happen when nevo got connected to sync weekly nevo data
 */

public class BigSyncEvent {

    public enum BIG_SYNC_EVENT{
        STARTED,
        STOPPED
    }

    public final BIG_SYNC_EVENT status;

    public BigSyncEvent(BIG_SYNC_EVENT status) {
        this.status = status;
    }

    public BIG_SYNC_EVENT getStatus() {
        return status;
    }
}
