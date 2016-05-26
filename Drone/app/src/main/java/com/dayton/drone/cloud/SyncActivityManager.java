package com.dayton.drone.cloud;

import android.content.Context;
import android.util.Log;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.model.Steps;
import com.dayton.drone.network.Constants;
import com.dayton.drone.network.request.UpdateStepsRequest;
import com.dayton.drone.network.request.model.CreateSteps;
import com.dayton.drone.network.request.model.StepsWithID;
import com.dayton.drone.network.response.model.CreateStepsModel;
import com.dayton.drone.network.request.CreateStepsRequest;
import com.dayton.drone.network.response.model.GetStepsModel;
import com.dayton.drone.network.request.GetStepsRequest;
import com.dayton.drone.network.response.model.StepsDetail;
import com.dayton.drone.network.response.model.UpdateStepsModel;
import com.dayton.drone.utils.Common;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
     * when big sync got end (FIFO == 0), invoke this function
     * @parameter theDay
     */
    public void launchSyncDailyHourlySteps(Date theDay)
    {
        final List<Steps> stepsList = getModel().getStepsDatabaseHelper().convertToNormalList(getModel().getStepsDatabaseHelper().get(getModel().getUser().getUserID(),Common.removeTimeFromDate(theDay)));
        if(stepsList.isEmpty()) {
            return;
        }
        final JSONArray hoursInDay = new JSONArray();
        try {
            //init a day all sample-point data as 0, total 24*12 sample-point
            for(int i=0;i<24;i++)
            {
                JSONArray minutesInHour = new JSONArray("[0,0,0,0,0,0,0,0,0,0,0,0]");
                hoursInDay.put(minutesInHour);
            }
            for(Steps steps:stepsList)
            {
                Date date = new Date(steps.getTimeFrame());
                int hour = date.getHours();
                int minute = date.getMinutes();
                hoursInDay.optJSONArray(hour).put(minute/5,steps.getSteps());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final CreateSteps createSteps = new CreateSteps();
        createSteps.setUid(Integer.parseInt(stepsList.get(0).getUserID()));
        createSteps.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(stepsList.get(0).getTimeFrame())));
        createSteps.setSteps(hoursInDay.toString());
        getModel().getRetrofitManager().execute(new CreateStepsRequest(createSteps, getModel().getRetrofitManager().getAccessToken()), new RequestListener<CreateStepsModel>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                spiceException.printStackTrace();
            }
            @Override
            public void onRequestSuccess(CreateStepsModel createStepsModel) {
                Log.i(TAG,"add steps result: "+createStepsModel.getMessage());
                if(createStepsModel.getMessage().contains("already exist")
                        && createStepsModel.getSteps()!=null )
                {
                    boolean needUpdate = false;
                    for(Steps steps:stepsList)
                    {
                        if(steps.getCloudID()!=createStepsModel.getSteps().getId())
                        {
                            needUpdate = true;
                            break;
                        }
                    }
                    //update if has one record at least
                    if(needUpdate)
                    {
                        StepsWithID stepsWithID = new StepsWithID();
                        stepsWithID.setId(createStepsModel.getSteps().getId());
                        stepsWithID.setUid(Integer.parseInt(stepsList.get(0).getUserID()));
                        stepsWithID.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date(stepsList.get(0).getTimeFrame())));
                        stepsWithID.setSteps(hoursInDay.toString());
                        updateSteps(stepsWithID, stepsList);
                    }
                }
                else if(createStepsModel.getMessage().equals("OK")
                        && createStepsModel.getStatus() == 1
                        && createStepsModel.getSteps()!=null)
                {
                    for(Steps steps:stepsList) {
                        if(steps.getCloudID()!=createStepsModel.getSteps().getId())
                        {
                            steps.setCloudID(createStepsModel.getSteps().getId());
                            getModel().getStepsDatabaseHelper().update(steps);
                        }
                    }
                }
            }
        });
    }

    private void updateSteps(StepsWithID stepsWithID, final List<Steps> stepsList)
    {
        getModel().getRetrofitManager().execute(new UpdateStepsRequest(stepsWithID, getModel().getRetrofitManager().getAccessToken()), new RequestListener<UpdateStepsModel>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                spiceException.printStackTrace();
            }

            @Override
            public void onRequestSuccess(UpdateStepsModel updateStepsModel) {
                Log.i(TAG,"upload steps result: "+updateStepsModel.getMessage());
                if(updateStepsModel.getMessage().equals("OK")
                        && updateStepsModel.getStatus()==1
                        && updateStepsModel.getSteps()!=null)
                {
                    for(Steps steps:stepsList)
                    {
                        if(steps.getCloudID()!=updateStepsModel.getSteps().getId())
                        {
                            steps.setCloudID(updateStepsModel.getSteps().getId());
                            getModel().getStepsDatabaseHelper().update(steps);
                        }
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
        downloadSteps(Common.getLast30Days(new Date()), new Date());
        uploadSleep(new Date());
        downloadSleep(new Date(), new Date());
    }

    private void uploadSteps( Date theDay)
    {
        Date start = Common.getLast7Days(theDay);
        for(long startDay = start.getTime();startDay<=theDay.getTime();startDay+=Common.ONEDAY)
        {
            List<Steps> stepsList = getModel().getStepsDatabaseHelper().convertToNormalList(getModel().getStepsDatabaseHelper().get(getModel().getUser().getUserID(),Common.removeTimeFromDate(new Date(startDay))));
            if(!stepsList.isEmpty() && (stepsList.get(0).getCloudID()<0 || stepsList.get(stepsList.size()-1).getCloudID()<0 ))
            {
                launchSyncDailyHourlySteps(new Date(startDay));
            }
        }
    }
    private void downloadSteps(final Date startDate, final Date endDate)
    {
        //TODO API should add a filter to query: ?startdate=...& enddate=...
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
                        //TODO getSteps() return format should be "[[1,2,3,4,5,6,7,8,9,10,11,12],[100,...],......]"
                        if(stepsDetail.getSteps()!=null
                                && stepsDetail.getSteps().startsWith("[[")
                                && stepsDetail.getSteps().endsWith("]]"))
                        {
                            try {
                                JSONArray hoursInDay = new JSONArray(stepsDetail.getSteps());
                                for(int hour =0;hour<hoursInDay.length();hour++)
                                {
                                    JSONArray minutesInHour = hoursInDay.optJSONArray(hour);
                                    for(int minute = 0;minute<minutesInHour.length();minute++)
                                    {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000000");
                                        try {
                                            Date date = sdf.parse(stepsDetail.getDate().getDate());
                                            Steps steps = new Steps();
                                            steps.setDate((Common.removeTimeFromDate(date)).getTime());
                                            String timeFrame = stepsDetail.getDate().getDate().split(" ")[0];
                                            timeFrame = timeFrame + " " + ((hour>=10)?(""+hour):("0"+hour)) + ":" + (((5*minute)>=10)?(""+5*minute):("0"+5*minute)) + ":00.000000";
                                            steps.setTimeFrame(sdf.parse(timeFrame).getTime());
                                            steps.setSteps(minutesInHour.optInt(minute));
                                            steps.setUserID(stepsDetail.getUid()+"");
                                            steps.setCloudID(stepsDetail.getId());
                                            getModel().getStepsDatabaseHelper().update(steps);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
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
