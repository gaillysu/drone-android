package com.dayton.drone.network.request;

import android.util.Log;

import com.dayton.drone.network.request.model.StepsWithID;
import com.dayton.drone.network.request.model.UpdateStepsObject;
import com.dayton.drone.network.request.model.UpdateStepsParameters;
import com.dayton.drone.network.response.model.UpdateStepsModel;
import com.dayton.drone.network.restapi.Drone;
import com.google.gson.Gson;

/**
 * Created by med on 16/5/3.
 */
public class UpdateStepsRequest extends BaseRequest<UpdateStepsModel,Drone> implements BaseRequest.BaseRetroRequestBody<UpdateStepsObject>{

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
