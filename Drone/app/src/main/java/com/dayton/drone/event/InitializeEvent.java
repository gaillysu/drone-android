package com.dayton.drone.event;

/**
 * this event happen when nevo got connected to set some parameters to nevo (set time, set user profile...)
 */
public class InitializeEvent {

    public enum INITIALIZE_STATUS{
        START,
        END
    }

    private final INITIALIZE_STATUS status;

    public InitializeEvent(INITIALIZE_STATUS status) {
        this.status = status;
    }

    public INITIALIZE_STATUS getStatus() {
        return status;
    }
}
