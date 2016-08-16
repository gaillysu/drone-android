package com.dayton.drone.event;

import net.medcorp.library.worldclock.City;

import java.util.List;

/**
 * Created by med on 16/5/23.
 */
public class WorldClockChangedEvent {
    final List<City> worldClockList;
    public WorldClockChangedEvent(List<City> worldClockList) {
        this.worldClockList = worldClockList;
    }

    public List<City> getWorldClockList() {
        return worldClockList;
    }
}
