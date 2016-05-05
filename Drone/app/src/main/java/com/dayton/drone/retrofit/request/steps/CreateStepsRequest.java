package com.dayton.drone.retrofit.request.steps;

import android.util.Log;

import com.dayton.drone.retrofit.model.Steps;
import com.dayton.drone.retrofit.request.BaseRetroRequest;
import com.dayton.drone.retrofit.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by med on 16/5/3.
 */
public class CreateStepsRequest extends BaseRetroRequest<CreateStepsModel,Drone> implements BaseRetroRequest.BaseRetroRequestBody<CreateStepsObject>{

    private Steps steps;
    private String token;

    public CreateStepsRequest(Steps steps,String token) {
        super(CreateStepsModel.class, Drone.class);
        this.steps = steps;
        this.token = token;
    }

    @Override
    public CreateStepsModel loadDataFromNetwork() throws Exception {
        return getService().stepsCreate(buildRequestBody(),buildAuthorization(),CONTENT_TYPE);
    }

    @Override
    public CreateStepsObject buildRequestBody() {
        CreateStepsObject object = new CreateStepsObject();
        object.setToken(token);
        CreateStepsParameters createStepsParameters = new CreateStepsParameters();
        createStepsParameters.setSteps(steps);
        object.setParams(createStepsParameters);
        Log.i(this.getClass().getSimpleName(), "object: " + new Gson().toJson(object));
        return object;
    }
}
