package com.dayton.drone.network.response.model;

import com.dayton.drone.network.response.BaseResponse;

/**
 * Created by med on 16/5/3.
 */
public class CreateStepsModel extends BaseResponse{
    private StepsDetail steps;

    public StepsDetail getSteps() {
        return steps;
    }

    public void setSteps(StepsDetail steps) {
        this.steps = steps;
    }
}
