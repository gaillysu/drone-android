package com.dayton.drone.event;

/**
 * this event happen when steps got change to refresh screen
 */

public class LittleSyncEvent {

    private final int steps;
    private final int goal;

    public LittleSyncEvent(int steps, int goal) {
        this.steps = steps;
        this.goal = goal;
    }

    public int getSteps() {
        return steps;
    }

    public int getGoal() {
        return goal;
    }
}
