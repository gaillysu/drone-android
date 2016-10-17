package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.StepsBean;
import com.dayton.drone.model.CanlendarWeek;
import com.dayton.drone.model.Steps;
import com.dayton.drone.utils.Common;

import net.medcorp.library.ble.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by karl-john on 17/11/15.
 */
public class StepsDatabaseHelper {

    private DatabaseHelper databaseHelper;

    public StepsDatabaseHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelperInstance(context);
        }
    }


    public Optional<Steps> add(Steps object) {
        Optional<Steps> stepsOptional = new Optional<>();
        try {
            StepsBean daoobject = convertToDao(object);
            daoobject.setHourlySteps(convertToDAOHourlySteps(object.getHourlySteps(),object));
            StepsBean stepsDAO = databaseHelper.getStepsBean().createIfNotExists(daoobject);
            if(stepsDAO != null)
            {
                stepsOptional.set(convertToNormal(stepsDAO));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stepsOptional;
    }


    public boolean update(Steps object) {

        int result = -1;
        try {
            List<StepsBean> stepsDAOList = databaseHelper.getStepsBean()
                    .queryBuilder().where().eq(StepsBean.fUserID, object.getUserID())
                    .and().eq(StepsBean.fDate, Common.removeTimeFromDate(new Date(object.getDate())).getTime())
                    .query();
            if(stepsDAOList.isEmpty()) return add(object)!=null;
            StepsBean daoobject = convertToDao(object);
            daoobject.setId(stepsDAOList.get(0).getId());
            daoobject.setHourlySteps(convertToDAOHourlySteps(stepsDAOList.get(0).getHourlySteps(), object));
            result = databaseHelper.getStepsBean().update(daoobject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result>=0;
    }


    public boolean remove(String userId,Date date) {
        try {
            List<StepsBean> stepsDAOList = databaseHelper.getStepsBean()
                    .queryBuilder().where().eq(StepsBean.fUserID, userId)
                    .and().eq(StepsBean.fDate,Common.removeTimeFromDate(date).getTime()).query();
            if(!stepsDAOList.isEmpty())
            {
                return databaseHelper.getStepsBean().delete(stepsDAOList)>=0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Optional<Steps> >  get(String userId,Date date) {
        List<Optional<Steps> > stepsList = new ArrayList<>();
        try {
            List<StepsBean> stepsDAOList = databaseHelper.getStepsBean()
                    .queryBuilder().where().eq(StepsBean.fUserID, userId)
                    .and().eq(StepsBean.fDate, Common.removeTimeFromDate(date).getTime())
                    .query();
            for(StepsBean stepsBean : stepsDAOList){
                Optional<Steps> stepsOptional = new Optional<>();
                stepsOptional.set(convertToNormal(stepsBean));
                stepsList.add(stepsOptional);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stepsList;
    }


    public List<Optional<Steps> > getAll(String userId) {
        List<Optional<Steps> > stepsList = new ArrayList<Optional<Steps> >();
        try {
            List<StepsBean> stepsDAOList = databaseHelper.getStepsBean()
                    .queryBuilder().orderBy(StepsBean.fDate,true).where().
                            eq(StepsBean.fUserID, userId).query();
            for(StepsBean stepsDAO : stepsDAOList){
                Optional<Steps> stepsOptional = new Optional<>();
                stepsOptional.set(convertToNormal(stepsDAO));
                stepsList.add(stepsOptional);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stepsList;
    }

    private StepsBean convertToDao(Steps steps){
        StepsBean stepsDao = new StepsBean();
        stepsDao.setUserID(steps.getUserID());
        stepsDao.setDate(steps.getDate());
        stepsDao.setHourlySteps(steps.getHourlySteps());
        stepsDao.setDistance(steps.getDistance());
        stepsDao.setCloudID(steps.getCloudID());
        stepsDao.setStepsGoal(steps.getStepsGoal());
        return stepsDao;
    }

    private Steps convertToNormal(StepsBean stepsDAO){
        Steps steps = new Steps(0,stepsDAO.getDate());
        steps.setUserID(stepsDAO.getUserID());
        steps.setDate(stepsDAO.getDate());
        steps.setHourlySteps(stepsDAO.getHourlySteps());
        steps.setDistance(stepsDAO.getDistance());
        steps.setCloudID(stepsDAO.getCloudID());
        steps.setStepsGoal(stepsDAO.getStepsGoal());
        return steps;
    }

    private String convertToDAOHourlySteps(String srcDAOHourlySteps,Steps steps)
    {
        String dstDAOHourlySteps = srcDAOHourlySteps;
        Date date = new Date(steps.getTimeFrame());
        int indexHour = date.getHours();
        int indexMinute = date.getMinutes()/5;  //range: 0~11
        try {
            JSONArray hourlySteps = new JSONArray(srcDAOHourlySteps);
            JSONArray minutesInHour = hourlySteps.optJSONArray(indexHour);
            minutesInHour.put(indexMinute,steps.getTimeFrameSteps());
            //fix null Object to 0 value for saving string length
            for(int k=0;k<indexMinute;k++)
            {
                if(minutesInHour.opt(k)==null)
                {
                    minutesInHour.put(k,0);
                }
            }
            dstDAOHourlySteps = hourlySteps.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dstDAOHourlySteps;
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
