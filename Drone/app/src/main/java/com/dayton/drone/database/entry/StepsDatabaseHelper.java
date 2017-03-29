package com.dayton.drone.database.entry;

import android.content.Context;


import com.dayton.drone.model.CanlendarWeek;
import com.dayton.drone.model.Steps;
import com.dayton.drone.utils.Common;

import net.medcorp.library.ble.util.Optional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

/**
 * Created by karl-john on 17/11/15.
 */
public class StepsDatabaseHelper {

    private Realm realm;

    public StepsDatabaseHelper(Context context) {
        realm = Realm.getDefaultInstance();
    }


    public Optional<Steps> add(Steps object) {
        realm.beginTransaction();
        Steps steps = realm.copyToRealm(object);
        realm.commitTransaction();
        return new Optional<>(steps);
    }

    public boolean update(Steps object) {
        Steps steps = realm.where(Steps.class).equalTo(Steps.fUserID, object.getUserID()).equalTo(Steps.fDate, Common.removeTimeFromDate(new Date(object.getDate())).getTime()).findFirst();
        if(steps==null) {
            return add(object).notEmpty();
        }
        realm.beginTransaction();
        copyToRealm(steps,object);
        realm.commitTransaction();
        return true;
    }

    public boolean remove(String userId,Date date) {
        Steps steps = realm.where(Steps.class).equalTo(Steps.fUserID, userId).equalTo(Steps.fDate,Common.removeTimeFromDate(date).getTime()).findFirst();
        if(steps!=null){
            realm.beginTransaction();
            steps.deleteFromRealm();
            realm.commitTransaction();
            return true;
        }
        return false;
    }

    public List<Optional<Steps> >  get(String userId,Date date) {
        List<Optional<Steps> > stepsList = new ArrayList<>();
        List<Steps> stepses = realm.where(Steps.class).equalTo(Steps.fUserID, userId).equalTo(Steps.fDate,Common.removeTimeFromDate(date).getTime()).findAll();
        for(Steps steps : stepses) {
            stepsList.add(new Optional<>(steps));
        }
        return stepsList;
    }


    public List<Optional<Steps> > getAll(String userId) {
        List<Optional<Steps> > stepsList = new ArrayList<>();
        List<Steps> stepses = realm.where(Steps.class).equalTo(Steps.fUserID, userId).findAll();
        for(Steps steps : stepses) {
            stepsList.add(new Optional<>(steps));
        }
        return stepsList;
    }

    private void copyToRealm(Steps stepsRealm,Steps object){
        stepsRealm.setUserID(object.getUserID());
        stepsRealm.setDate(object.getDate());
        stepsRealm.setHourlySteps(object.getHourlySteps());
        stepsRealm.setDistance(object.getDistance());
        stepsRealm.setCloudID(object.getCloudID());
        stepsRealm.setStepsGoal(object.getStepsGoal());
    }

    public List<Steps> convertToNormalList(List<Optional<Steps>> optionals) {
        List<Steps> stepsList = new ArrayList<>();
        for (Optional<Steps> stepsOptional: optionals) {
            if (stepsOptional.notEmpty() && stepsOptional.get().getDailySteps()>0){
                stepsList.add(stepsOptional.get());
            }
        }
        return stepsList;
    }

    public List<Steps> getThisWeekSteps(String userId,Date date)
    {
       CanlendarWeek canlendarWeek =  Common.getThisweek(date);
       return getStepsBetweenDate(userId,canlendarWeek.getWeekStart(),canlendarWeek.getWeekEnd());
    }


    public List<Steps> getLastWeekSteps(String userId,Date date)
    {
        CanlendarWeek canlendarWeek =  Common.getLastweek(date);
        return getStepsBetweenDate(userId,canlendarWeek.getWeekStart(),canlendarWeek.getWeekEnd());
    }

    public List<Steps> getLastMonthSteps(String userId,Date date)
    {
        return getStepsBetweenDate(userId,Common.getLast30Days(date),date);
    }

    public List<Steps> getStepsBetweenDate(String userId,Date startDate,Date endDate)
    {
        List<Steps> stepsList = new ArrayList<>();

        Date start = Common.removeTimeFromDate(startDate);
        Date end = Common.removeTimeFromDate(endDate);

        for(long timeStamp = start.getTime();timeStamp<=end.getTime();timeStamp+=Common.ONE_DAY)
        {
            stepsList.addAll(convertToNormalList(get(userId,new Date(timeStamp))));
        }
        return stepsList;
    }

}
