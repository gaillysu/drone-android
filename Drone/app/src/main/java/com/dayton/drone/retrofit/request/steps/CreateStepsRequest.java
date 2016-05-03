package com.dayton.drone.retrofit.request.steps;

import com.dayton.drone.retrofit.model.Steps;
import com.dayton.drone.retrofit.request.BaseRetroRequest;
import com.dayton.drone.retrofit.restapi.Drone;

/**
 * Created by med on 16/5/3.
 */
public class CreateStepsRequest extends BaseRetroRequest<CreateStepsModel,Drone> implements BaseRetroRequest.BaseRetroRequestBody<CreateStepsObject>{

    private Steps[] steps;
    private String token;

    public CreateStepsRequest(Steps[] steps,String token) {
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
        CreateStepsObject createStepsObject = new CreateStepsObject();
        createStepsObject.setToken(token);
        CreateStepsParameters createStepsParameters = new CreateStepsParameters();
        createStepsParameters.setSteps(steps);
        createStepsObject.setParams(createStepsParameters);
        return createStepsObject;
    }
}
