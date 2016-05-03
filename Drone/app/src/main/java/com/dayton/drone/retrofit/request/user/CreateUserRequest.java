package com.dayton.drone.retrofit.request.user;

import com.dayton.drone.retrofit.model.User;
import com.dayton.drone.retrofit.restapi.Drone;
import com.dayton.drone.retrofit.request.BaseRetroRequest;

/**
 * Created by med on 16/5/3.
 */
public class CreateUserRequest extends BaseRetroRequest<CreateUserModel,Drone> implements BaseRetroRequest.BaseRetroRequestBody<CreateUserObject> {

    private String token;
    private User   user;

    public CreateUserRequest(User user,String token) {
        super(CreateUserModel.class, Drone.class);
        this.user  = user;
        this.token = token;
    }

    @Override
    public CreateUserModel loadDataFromNetwork() throws Exception {
        return getService().userCreate(buildRequestBody(), buildAuthorization(), CONTENT_TYPE);
    }

    @Override
    public CreateUserObject buildRequestBody() {
        CreateUserObject createUserObject = new CreateUserObject();
        createUserObject.setToken(token);
        CreateParameters parameters = new CreateParameters();
        parameters.setUser(user);
        createUserObject.setParams(parameters);
        return createUserObject;
    }
}
