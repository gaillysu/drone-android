package com.dayton.drone.database.entry;

import android.content.Context;


import com.dayton.drone.database.bean.StepsBean;
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

    public StepsDatabaseHelper(Context context) {

    }

    public Optional<Steps> add(Steps object) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        StepsBean stepsBean = realm.copyToRealm(convertToDao(new StepsBean(),object));
        realm.commitTransaction();
        return new Optional<>(convertToNormal(new Steps(0,stepsBean.getDate()),stepsBean));
    }

    public boolean update(Steps object) {
        Realm realm = Realm.getDefaultInstance();
        StepsBean stepsBean = realm.where(StepsBean.class).equalTo(StepsBean.fUserID, object.getUserID()).equalTo(StepsBean.fDate, Common.removeTimeFromDate(new Date(object.getDate())).getTime()).findFirst();
        if(stepsBean==null) {
            return add(object).notEmpty();
        }
        realm.beginTransaction();
        convertToDao(stepsBean,object);
        realm.commitTransaction();
        return true;
    }

    public boolean remove(String userId,Date date) {
        Realm realm = Realm.getDefaultInstance();
        StepsBean stepsBean = realm.where(StepsBean.class).equalTo(StepsBean.fUserID, userId).equalTo(StepsBean.fDate,Common.removeTimeFromDate(date).getTime()).findFirst();
        if(stepsBean!=null){
            realm.beginTransaction();
            stepsBean.deleteFromRealm();
            realm.commitTransaction();
            return true;
        }
        return false;
    }

    public List<Optional<Steps> >  get(String userId,Date date) {
        Realm realm = Realm.getDefaultInstance();
        List<Optional<Steps> > stepsList = new ArrayList<>();
        List<StepsBean> all = realm.where(StepsBean.class).equalTo(StepsBean.fUserID, userId).equalTo(StepsBean.fDate,Common.removeTimeFromDate(date).getTime()).findAll();
        for(StepsBean stepsBean : all) {
            stepsList.add(new Optional<>(convertToNormal(new Steps(0,stepsBean.getDate()),stepsBean)));
        }
        return stepsList;
    }


    public List<Optional<Steps> > getAll(String userId) {
        Realm realm = Realm.getDefaultInstance();
        List<Optional<Steps> > stepsList = new ArrayList<>();
        List<StepsBean> all = realm.where(StepsBean.class).equalTo(StepsBean.fUserID, userId).findAll();
        for(StepsBean stepsBean : all) {
            stepsList.add(new Optional<>(convertToNormal(new Steps(0,stepsBean.getDate()),stepsBean)));
        }
        return stepsList;
    }

    private StepsBean convertToDao(StepsBean stepsDao, Steps steps){
        stepsDao.setUserID(steps.getUserID());
        stepsDao.setDate(steps.getDate());
        stepsDao.setHourlySteps(steps.getHourlySteps());
        stepsDao.setDistance(steps.getDistance());
        stepsDao.setCloudID(steps.getCloudID());
        stepsDao.setStepsGoal(steps.getStepsGoal());
        return stepsDao;
    }

    private Steps convertToNormal(Steps steps,StepsBean stepsDAO){
        steps.setUserID(stepsDAO.getUserID());
        steps.setDate(stepsDAO.getDate());
        steps.setHourlySteps(stepsDAO.getHourlySteps());
        steps.setDistance(stepsDAO.getDistance());
        steps.setCloudID(stepsDAO.getCloudID());
        steps.setStepsGoal(stepsDAO.getStepsGoal());
        return steps;
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
