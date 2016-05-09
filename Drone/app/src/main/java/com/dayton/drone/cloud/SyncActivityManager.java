package com.dayton.drone.cloud;

import android.content.Context;
import android.util.Log;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.retrofit.Constants;
import com.dayton.drone.retrofit.model.Steps;
import com.dayton.drone.retrofit.model.StepsModel;
import com.dayton.drone.retrofit.model.StepsWithID;
import com.dayton.drone.retrofit.request.steps.CreateStepsModel;
import com.dayton.drone.retrofit.request.steps.CreateStepsRequest;
import com.dayton.drone.retrofit.request.steps.GetStepsModel;
import com.dayton.drone.retrofit.request.steps.GetStepsRequest;
import com.dayton.drone.retrofit.request.steps.UpdateStepsModel;
import com.dayton.drone.retrofit.request.steps.UpdateStepsRequest;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by med on 16/4/29.
 *
 * this class must be initialized in the ApplicationModel class as a global instance
 *
 */
public class SyncActivityManager {

    private final String TAG = "SyncActivityManager";
    private Context context;
    final long INTERVAL_DATE = 365 * 24 * 60 * 60 *1000l;//user can get all data in a year

    public SyncActivityManager(Context context)
    {
        this.context = context;
    }
    private ApplicationModel getModel() {return (ApplicationModel)context;}

    /**
     * when today's steps got change, invoke it
     */
    public void launchSyncDailySteps(final Steps steps)
    {
        getModel().getRetrofitManager().execute(new CreateStepsRequest(steps, getModel().getRetrofitManager().getAccessToken()), new RequestListener<CreateStepsModel>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                spiceException.printStackTrace();
            }

            @Override
            public void onRequestSuccess(CreateStepsModel createStepsModel) {
                    if(createStepsModel.getSteps()!=null)
                    {
                        StepsWithID stepsWithID = new StepsWithID();
                        stepsWithID.setId(createStepsModel.getSteps().getId());
                        stepsWithID.setUid(createStepsModel.getSteps().getUid());
                        stepsWithID.setDate(steps.getDate());
                        stepsWithID.setSteps(steps.getSteps());
                        if(createStepsModel.getSteps().getSteps()==stepsWithID.getSteps()) {
                            Log.i(TAG,"update steps to"+createStepsModel.getSteps().getSteps());
                            return;
                        }
                    }
            }
        });
    }

    /**
     * when user login, invoke it
     */
    public void launchSyncAll(){
        uploadSteps(new Date());
        downloadSteps(new Date(), new Date());
        uploadSleep(new Date());
        downloadSleep(new Date(), new Date());
    }

    private void uploadSteps( Date theDay)
    {

    }
    private void downloadSteps(final Date startDate, final Date endDate)
    {
        getModel().getRetrofitManager().execute(new GetStepsRequest(getModel().getUser().getUserID()), new RequestListener<GetStepsModel>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                spiceException.printStackTrace();
            }

            @Override
            public void onRequestSuccess(GetStepsModel getStepsModel) {
                if(getStepsModel.getStatus() == Constants.STATUS_CODE.STATUS_SUCCESS) {
                    Log.i(TAG,getStepsModel.getMessage());
                }
            }
        });
    }

    private void uploadSleep(Date theDay)
    {

    }
    private void downloadSleep(final Date startDate, final Date endDate)
    {

    }
}
