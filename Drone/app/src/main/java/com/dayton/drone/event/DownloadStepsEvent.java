package com.dayton.drone.event;

/**
 * Created by med on 16/6/21.
 */
public class DownloadStepsEvent {
    public DownloadStepsEvent(DOWNLOAD_STEPS_EVENT status) {
        this.status = status;
    }

    public DOWNLOAD_STEPS_EVENT getStatus() {
        return status;
    }

    public enum DOWNLOAD_STEPS_EVENT{
        STARTED,
        STOPPED
    }

    final DOWNLOAD_STEPS_EVENT status;

}
