package com.dayton.drone.event;

import java.util.Date;

/**
 * this event happen when nevo got connected to sync weekly nevo data
 */

public class BigSyncEvent {

    public enum BIG_SYNC_EVENT{
        STARTED,
        STOPPED
    }

    private final Date startSyncDate; // the big sync start date, end date is now()
    private final BIG_SYNC_EVENT status;

    public BigSyncEvent(Date startSyncDate, BIG_SYNC_EVENT status) {
        this.startSyncDate = startSyncDate;
        this.status = status;
    }

    public BIG_SYNC_EVENT getStatus() {
        return status;
    }

    public Date getStartSyncDate() {
        return startSyncDate;
    }
}
