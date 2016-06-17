package com.dayton.drone.cloud;

import android.content.Context;
import android.util.Log;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.event.BigSyncEvent;
import com.dayton.drone.event.ProfileChangedEvent;
import com.dayton.drone.model.Steps;
import com.dayton.drone.network.Constants;
import com.dayton.drone.network.request.CreateStepsRequest;
import com.dayton.drone.network.request.GetStepsRequest;
import com.dayton.drone.network.request.UpdateStepsRequest;
import com.dayton.drone.network.request.UpdateUserRequest;
import com.dayton.drone.network.request.model.CreateSteps;
import com.dayton.drone.network.request.model.StepsWithID;
import com.dayton.drone.network.request.model.UpdateUser;
import com.dayton.drone.network.response.model.CreateStepsModel;
import com.dayton.drone.network.response.model.GetStepsModel;
import com.dayton.drone.network.response.model.StepsDetail;
import com.dayton.drone.network.response.model.UpdateStepsModel;
import com.dayton.drone.network.response.model.UpdateUserModel;
import com.dayton.drone.utils.Common;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by med on 16/4/29.
 *
 * this class must be initialized in the ApplicationModel class as a global instance
 *
 */
public class SyncActivityManager {

    private final String TAG = "SyncActivityManager";
    private Context context;
    final long INTERVAL_DATE = 365 * 24 * 60 * 60 *1000L;//user can get all data in a year

    public SyncActivityManager(Context context)
    {
        this.context = context;
        EventBus.getDefault().register(this);
    }
    private ApplicationModel getModel() {return (ApplicationModel)context;}

    private void launchSyncDailyHourlySteps(Date theDay)
    {
        final List<Steps> stepsList = getModel().getStepsDatabaseHelper().convertToNormalList(getModel().getStepsDatabaseHelper().get(getModel().getUser().getUserID(),Common.removeTimeFromDate(theDay)));
        if(stepsList.isEmpty()) {
            return;
        }
        final CreateSteps createSteps = new CreateSteps();
        createSteps.setUid(Integer.parseInt(stepsList.get(0).getUserID()));
        createSteps.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date(stepsList.get(0).getTimeFrame())));
        createSteps.setSteps(stepsList.get(0).getHourlySteps());
        createSteps.setActive_time(stepsList.get(0).getDailyActiveTime());
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
                    int totalServerDailySteps = 0;
                    try {
                        JSONArray jsonArray = new JSONArray(createStepsModel.getSteps().getSteps());
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            JSONArray stepsInHour = jsonArray.optJSONArray(i);
                            for(int j=0;j<stepsInHour.length();j++)
                            {
                                totalServerDailySteps += stepsInHour.optInt(j);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(stepsList.get(0).getCloudID()!=createStepsModel.getSteps().getId()
                            || totalServerDailySteps!= stepsList.get(0).getDailySteps())
                    {
                        needUpdate = true;
                    }
                    //update if has one record at least
                    if(needUpdate)
                    {
                        StepsWithID stepsWithID = new StepsWithID();
                        stepsWithID.setId(createStepsModel.getSteps().getId());
                        stepsWithID.setUid(Integer.parseInt(stepsList.get(0).getUserID()));
                        stepsWithID.setDate(new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date(stepsList.get(0).getTimeFrame())));
                        stepsWithID.setSteps(stepsList.get(0).getHourlySteps());
                        updateSteps(stepsWithID, stepsList);
                    }
                }
                else if(createStepsModel.getMessage().equals("OK")
                        && createStepsModel.getStatus() == 1
                        && createStepsModel.getSteps()!=null)
                {
                    if(stepsList.get(0).getCloudID()!=createStepsModel.getSteps().getId())
                    {
                        stepsList.get(0).setCloudID(createStepsModel.getSteps().getId());
                        getModel().getStepsDatabaseHelper().update(stepsList.get(0));
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

                    if(stepsList.get(0).getCloudID()!=updateStepsModel.getSteps().getId())
                    {
                        stepsList.get(0).setCloudID(updateStepsModel.getSteps().getId());
                        getModel().getStepsDatabaseHelper().update(stepsList.get(0));
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
//        uploadSleep(new Date());
//        downloadSleep(new Date(), new Date());
    }

    private void uploadSteps( Date theDay)
    {
        Date start = Common.getLast7Days(theDay);
        for(long startDay = start.getTime();startDay<=theDay.getTime();startDay+=Common.ONE_DAY)
        {
            List<Steps> stepsList = getModel().getStepsDatabaseHelper().convertToNormalList(getModel().getStepsDatabaseHelper().get(getModel().getUser().getUserID(),Common.removeTimeFromDate(new Date(startDay))));
            if(!stepsList.isEmpty() && (stepsList.get(0).getCloudID())<0)
            {
                launchSyncDailyHourlySteps(new Date(startDay));
            }
        }
    }
    private void downloadSteps(final Date startDate, final Date endDate)
    {
        //TODO API should add a filter to query: ?startdate=...& enddate=...
        long start_date = Common.removeTimeFromDate(startDate).getTime()/1000;
        long end_date = Common.removeTimeFromDate(endDate).getTime()/1000;
        getModel().getRetrofitManager().execute(new GetStepsRequest(getModel().getUser().getUserID(),getModel().getRetrofitManager().getAccessToken(),start_date,end_date),new RequestListener<GetStepsModel>() {
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
                        //TODO getSteps() return format should be "[[1,2,...12],[2,2,3,...12],...,[22...],[23...],[24...]]"
                        if(stepsDetail.getSteps()!=null
                                && stepsDetail.getSteps().startsWith("[[")
                                && stepsDetail.getSteps().endsWith("]]"))
                        {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000000", Locale.US);
                            try {
                                Date date = sdf.parse(stepsDetail.getDate().getDate());
                                Steps steps = new Steps(0,0);
                                steps.setHourlySteps(stepsDetail.getSteps());
                                steps.setDate((Common.removeTimeFromDate(date)).getTime());
                                steps.setUserID(stepsDetail.getUid()+"");
                                steps.setCloudID(stepsDetail.getId());
                                getModel().getStepsDatabaseHelper().update(steps);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

    }

    @Subscribe
    public void onEvent(final BigSyncEvent event) {
        if(event.getStatus() == BigSyncEvent.BIG_SYNC_EVENT.STOPPED)
        {
            Date today = new Date();
            for(long start = event.getStartSyncDate().getTime();start<=today.getTime();start+= Common.ONE_DAY)
            {
                launchSyncDailyHourlySteps(new Date(start));
            }
        }
    }

    @Subscribe
    public void onEvent(ProfileChangedEvent profileChangedEvent) {
        UpdateUser updateUser = new UpdateUser();
        updateUser.setId(Integer.parseInt(profileChangedEvent.getUser().getUserID()));
        updateUser.setFirst_name(profileChangedEvent.getUser().getFirstName());
        updateUser.setLast_name(profileChangedEvent.getUser().getLastName());
        updateUser.setEmail(profileChangedEvent.getUser().getUserEmail());
        updateUser.setLength(profileChangedEvent.getUser().getHeight());
        updateUser.setBirthday(profileChangedEvent.getUser().getBirthday());
        updateUser.setSex(profileChangedEvent.getUser().getGender());
        getModel().getRetrofitManager().execute(new UpdateUserRequest(updateUser, getModel().getRetrofitManager().getAccessToken()), new RequestListener<UpdateUserModel>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                spiceException.printStackTrace();
            }

            @Override
            public void onRequestSuccess(UpdateUserModel updateUserModel) {
                Log.i(TAG,updateUserModel.getMessage());
                if(updateUserModel.getStatus()==1) {
                    //Todo hello?
                }
            }
        });
    }
}
