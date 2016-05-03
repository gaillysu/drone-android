package com.dayton.drone.retrofit.request.steps;

import com.dayton.drone.retrofit.model.StepsWithID;

/**
 * Created by med on 16/5/3.
 */
public class UpdateStepsParameters {
    StepsWithID[] steps;

    public StepsWithID[] getSteps() {
        return steps;
    }

    public void setSteps(StepsWithID[] steps) {
        this.steps = steps;
    }
}
