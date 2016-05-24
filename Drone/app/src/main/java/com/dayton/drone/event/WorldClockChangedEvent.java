package com.dayton.drone.event;

import com.dayton.drone.model.WorldClock;

import java.util.List;

/**
 * Created by med on 16/5/23.
 */
public class WorldClockChangedEvent {
    final List<WorldClock> worldClockList;
    public WorldClockChangedEvent(List<WorldClock> worldClockList) {
        this.worldClockList = worldClockList;
    }

    public List<WorldClock> getWorldClockList() {
        return worldClockList;
    }
}
