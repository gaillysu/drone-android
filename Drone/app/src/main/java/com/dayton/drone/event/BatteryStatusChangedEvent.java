package com.dayton.drone.event;

/**
 * Created by med on 16/4/14.
 */
public class BatteryStatusChangedEvent {
    private final byte state;
    private final byte level;

    public BatteryStatusChangedEvent(byte state, byte level) {
        this.state = state;
        this.level = level;
    }

    public byte getState() {
        return state;
    }

    public byte getLevel() {
        return level;
    }
}
