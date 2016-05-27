package com.dayton.drone.event;

/**
 * Created by med on 16/5/27.
 */
public class StepsGoalChangedEvent {
    private final int stepsGoal;

    public StepsGoalChangedEvent(int stepsGoal) {
        this.stepsGoal = stepsGoal;
    }

    public int getStepsGoal() {
        return stepsGoal;
    }
}
