package com.dayton.drone.retrofit.request.steps;

import android.util.Log;

import com.dayton.drone.retrofit.model.Steps;
import com.dayton.drone.retrofit.model.StepsWithID;
import com.dayton.drone.retrofit.request.BaseRetroRequest;
import com.dayton.drone.retrofit.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by med on 16/5/3.
 */
public class UpdateStepsRequest extends BaseRetroRequest<UpdateStepsModel,Drone> implements BaseRetroRequest.BaseRetroRequestBody<UpdateStepsObject>{

    private StepsWithID steps;
    private String token;

    public UpdateStepsRequest(StepsWithID steps, String token) {
        super(UpdateStepsModel.class, Drone.class);
        this.steps = steps;
        this.token = token;
    }

    @Override
    public UpdateStepsModel loadDataFromNetwork() throws Exception {
        return getService().stepsUpdate(buildRequestBody(), buildAuthorization(), CONTENT_TYPE);
    }

    @Override
    public UpdateStepsObject buildRequestBody() {
        UpdateStepsObject object = new UpdateStepsObject();
        object.setToken(token);
        UpdateStepsParameters updateStepsParameters = new UpdateStepsParameters();
        updateStepsParameters.setSteps(steps);
        object.setParams(updateStepsParameters);
        Log.i(this.getClass().getSimpleName(), "object: " + new Gson().toJson(object));
        return object;
    }
}
