package com.dayton.drone.cloud;

import android.content.Context;
import android.util.Log;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.model.Steps;
import com.dayton.drone.network.Constants;
import com.dayton.drone.network.request.model.CreateSteps;
import com.dayton.drone.network.request.model.StepsWithID;
import com.dayton.drone.network.response.model.CreateStepsModel;
import com.dayton.drone.network.request.CreateStepsRequest;
import com.dayton.drone.network.response.model.GetStepsModel;
import com.dayton.drone.network.request.GetStepsRequest;
import com.dayton.drone.network.response.model.StepsDetail;
import com.dayton.drone.utils.Common;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    public void launchSyncDailyAccumulateSteps(final CreateSteps steps)
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
                            Log.i(TAG,"update steps to "+createStepsModel.getSteps().getSteps());
                            return;
                        }
                    }
            }
        });
    }

    /**
     * when big sync got start or app receive  Constants.SystemEvent.ActivityDataAvailable, invoke this function
     * @param steps
     */
    public void launchSyncDailyTimeFrameSteps(final Steps steps)
    {
        final CreateSteps createSteps = new CreateSteps();
        createSteps.setUid(Integer.parseInt(steps.getUserID()));
        createSteps.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(steps.getTimeFrame())));
        createSteps.setSteps(steps.getSteps());
        getModel().getRetrofitManager().execute(new CreateStepsRequest(createSteps, getModel().getRetrofitManager().getAccessToken()), new RequestListener<CreateStepsModel>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                spiceException.printStackTrace();
            }
            @Override
            public void onRequestSuccess(CreateStepsModel createStepsModel) {
                Log.i(TAG,"upload time frame steps result: "+createStepsModel.getMessage());
                if(createStepsModel.getStatus() == 1 && createStepsModel.getSteps()!=null)
                {
                    steps.setCloudID(createStepsModel.getSteps().getId());
                    getModel().getStepsDatabaseHelper().update(steps);
                }
            }
        });
    }

    /**
     * when user login, invoke it
     */
    public void launchSyncAll(){
        uploadSteps(new Date());
        downloadSteps(Common.getLast30Days(new Date()), new Date());
        uploadSleep(new Date());
        downloadSleep(new Date(), new Date());
    }

    private void uploadSteps( Date theDay)
    {
        Date start = Common.getLast7Days(theDay);
        List<Steps> stepsList = getModel().getStepsDatabaseHelper().getStepsBetweenDate(getModel().getUser().getUserID(),start,theDay);
        for(final Steps steps:stepsList)
        {
            //if no cloud sync, do it
            if(steps.getCloudID()< 0)
            {
                launchSyncDailyTimeFrameSteps(steps);
            }
        }
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
                    for(StepsDetail stepsDetail:getStepsModel.getSteps())
                    {
                        if(stepsDetail.getSteps()>0)
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000000");
                            try {
                                Date date = sdf.parse(stepsDetail.getDate().getDate());
                                //exclude those records that accumulate for every day, only save the records with time frame
                                if(!(date.getHours()==0 && date.getMinutes()==0 && date.getSeconds()==0))
                                {
                                    Steps steps = new Steps();
                                    steps.setTimeFrame(date.getTime());
                                    steps.setDate((Common.removeTimeFromDate(date)).getTime());
                                    steps.setSteps(stepsDetail.getSteps());
                                    steps.setUserID(stepsDetail.getUid()+"");
                                    steps.setCloudID(stepsDetail.getId());
                                    getModel().getStepsDatabaseHelper().update(steps);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
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
