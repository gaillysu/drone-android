package com.dayton.drone.event;

import com.dayton.drone.model.Steps;

/**
 * Created by med on 16/5/25.
 */
public class TimeFramePacketReceivedEvent {
    final private Steps steps;

    public TimeFramePacketReceivedEvent(Steps steps) {
        this.steps = steps;
    }

    public Steps getSteps() {
        return steps;
    }
}
