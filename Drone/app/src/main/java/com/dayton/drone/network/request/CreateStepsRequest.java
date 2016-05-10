package com.dayton.drone.network.request;

import android.util.Log;

import com.dayton.drone.network.request.model.CreateSteps;
import com.dayton.drone.network.request.model.CreateStepsObject;
import com.dayton.drone.network.request.model.CreateStepsParameters;
import com.dayton.drone.network.response.model.CreateStepsModel;
import com.dayton.drone.network.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by med on 16/5/3.
 */
public class CreateStepsRequest extends BaseRequest<CreateStepsModel,Drone> implements BaseRequest.BaseRetroRequestBody<CreateStepsObject>{

    private CreateSteps steps;
    private String token;

    public CreateStepsRequest(CreateSteps steps,String token) {
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
