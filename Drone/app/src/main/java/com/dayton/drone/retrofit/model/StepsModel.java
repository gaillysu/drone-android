package com.dayton.drone.retrofit.model;

/**
 * Created by med on 16/5/3.
 */
public class StepsModel extends AbstractResponse{
    StepsDetailModel steps;

    public StepsDetailModel getSteps() {
        return steps;
    }

    public void setSteps(StepsDetailModel steps) {
        this.steps = steps;
    }

}
