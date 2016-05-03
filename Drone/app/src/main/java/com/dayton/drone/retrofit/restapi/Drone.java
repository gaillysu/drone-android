package com.dayton.drone.retrofit.restapi;

import com.dayton.drone.retrofit.request.steps.CreateStepsModel;
import com.dayton.drone.retrofit.request.steps.CreateStepsObject;
import com.dayton.drone.retrofit.request.steps.UpdateStepsModel;
import com.dayton.drone.retrofit.request.steps.UpdateStepsObject;
import com.dayton.drone.retrofit.request.user.CreateUserModel;
import com.dayton.drone.retrofit.request.user.CreateUserObject;
import com.dayton.drone.retrofit.request.user.LoginUserModel;
import com.dayton.drone.retrofit.request.user.LoginUserObject;

import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;

/**
 * Created by med on 16/4/29.
 */
public interface Drone {

    @POST("/user/create")
    CreateUserModel userCreate(@Body CreateUserObject object, @Header("Authorization") String auth, @Header("Content-Type") String type);

    @POST("/user/login")
    LoginUserModel userLogin(@Body LoginUserObject object,@Header("Authorization") String auth,@Header("Content-Type") String type);

    @POST("/steps/create")
    CreateStepsModel stepsCreate(@Body CreateStepsObject object,@Header("Authorization") String auth,@Header("Content-Type") String type);


    @PUT("/steps/update")
    UpdateStepsModel stepsUpdate(@Body UpdateStepsObject object,@Header("Authorization") String auth,@Header("Content-Type") String type);

}
