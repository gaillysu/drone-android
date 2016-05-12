package com.dayton.drone.network.restapi;

import com.dayton.drone.network.request.model.LoginUserObject;
import com.dayton.drone.network.request.model.CreateUserObject;
import com.dayton.drone.network.request.model.CreateStepsObject;
import com.dayton.drone.network.request.model.UpdateStepsObject;
import com.dayton.drone.network.response.model.CreateStepsModel;
import com.dayton.drone.network.response.model.GetStepsModel;
import com.dayton.drone.network.response.model.UpdateStepsModel;
import com.dayton.drone.network.response.model.CreateUserModel;
import com.dayton.drone.network.response.model.LoginUserModel;


import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.GET;

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

    @GET("/steps/user/{USER_ID}")
    GetStepsModel stepsGet(@Path("USER_ID") String userID,@Header("Authorization") String auth,@Header("Content-Type") String type);
}