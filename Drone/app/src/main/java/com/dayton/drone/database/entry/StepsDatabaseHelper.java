package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.StepsBean;
import com.dayton.drone.modle.Steps;

import net.medcorp.library.ble.util.Optional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by karl-john on 17/11/15.
 */
public class StepsDatabaseHelper implements iEntryDatabaseHelper<Steps> {

    private DatabaseHelper databaseHelper;

    private StepsDatabaseHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelperInstance(context);
        }
    }

    @Override
    public Optional<Steps> add(Steps object) {
        Optional<Steps> stepsOptional = new Optional<>();
        try {
            StepsBean stepsDAO = databaseHelper.getStepsBean().createIfNotExists(convertToDao(object));
            if(stepsDAO != null)
            {
                stepsOptional.set(convertToNormal(stepsDAO));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stepsOptional;
    }

    @Override
    public boolean update(Steps object) {

        int result = -1;
        try {
            List<StepsBean> stepsDAOList = databaseHelper.getStepsBean()
                    .queryBuilder().where().eq(StepsBean.fNevoUserID, object.getNevoUserID())
                    .and().eq(StepsBean.fDate, object.getDate()).query();
            if(stepsDAOList.isEmpty()) return add(object)!=null;
            StepsBean daoobject = convertToDao(object);
            daoobject.setID(stepsDAOList.get(0).getID());
            result = databaseHelper.getStepsBean().update(daoobject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result>=0;
    }

    @Override
    public boolean remove(String userId,Date date) {
        try {
            List<StepsBean> stepsDAOList = databaseHelper.getStepsBean()
                    .queryBuilder().where().eq(StepsBean.fNevoUserID, userId)
                    .and().eq(StepsBean.fDate,date.getTime()).query();
            if(!stepsDAOList.isEmpty())
            {
                return databaseHelper.getStepsBean().delete(stepsDAOList)>=0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Optional<Steps>> get(String userId) {
        return getAll(userId);
    }

    @Override
    public Optional<Steps>  get(String userId,Date date) {
        List<Optional<Steps> > stepsList = new ArrayList<Optional<Steps> >();
        try {
            List<StepsBean> stepsDAOList = databaseHelper.getStepsBean()
                    .queryBuilder().where().eq(StepsBean.fNevoUserID, userId)
                    .and().eq(StepsBean.fDate, date.getTime()).query();
            for(StepsBean stepsBean : stepsDAOList){
                Optional<Steps> stepsOptional = new Optional<>();
                stepsOptional.set(convertToNormal(stepsBean));
                stepsList.add(stepsOptional);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stepsList.isEmpty()? new Optional<Steps>() : stepsList.get(0);
    }

    @Override
    public List<Optional<Steps> > getAll(String userId) {
        List<Optional<Steps> > stepsList = new ArrayList<Optional<Steps> >();
        try {
            List<StepsBean> stepsDAOList = databaseHelper.getStepsBean()
                    .queryBuilder().orderBy(StepsBean.fDate,false).where().
                            eq(StepsBean.fNevoUserID, userId).query();
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

    public List<Steps> getNeedSyncSteps(String userId)
    {
        List<Steps> stepsList = new ArrayList<Steps>();
        try {
            List<StepsBean> stepsDAOList = databaseHelper.getStepsBean().queryBuilder()
                    .orderBy(StepsBean.fDate, false).where().eq(StepsBean.fNevoUserID, userId).and()
                    .eq(StepsBean.fValidicRecordID, "0").query();
            for(StepsBean stepsBean : stepsDAOList){
                stepsList.add(convertToNormal(stepsBean));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stepsList;
    }

    public boolean isFoundInLocalSteps(int activity_id)
    {
        try {
            List<StepsBean> stepsDAOList = databaseHelper.getStepsBean().queryBuilder()
                    .where().eq(StepsBean.fID, activity_id).query();
            return !stepsDAOList.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private StepsBean convertToDao(Steps steps){
        StepsBean stepsDao = new StepsBean();
        stepsDao.setID(steps.getiD());
        stepsDao.setNevoUserID(steps.getNevoUserID());
        stepsDao.setCreatedDate(steps.getCreatedDate());
        stepsDao.setDate(steps.getDate());
        stepsDao.setSteps(steps.getSteps());
        stepsDao.setWalkSteps(steps.getWalkSteps());
        stepsDao.setRunSteps(steps.getRunSteps());
        stepsDao.setDistance(steps.getDistance());
        stepsDao.setWalkDistance(steps.getWalkDistance());
        stepsDao.setRunDistance(steps.getRunDistance());
        stepsDao.setWalkDuration(steps.getWalkDuration());
        stepsDao.setRunDuration(steps.getRunDuration());
        stepsDao.setCalories(steps.getCalories());
        stepsDao.setHourlySteps(steps.getHourlySteps());
        stepsDao.setHourlyDistance(steps.getHourlyDistance());
        stepsDao.setHourlyCalories(steps.getHourlyCalories());
        stepsDao.setInZoneTime(steps.getInZoneTime());
        stepsDao.setOutZoneTime(steps.getOutZoneTime());
        stepsDao.setNoActivityTime(steps.getNoActivityTime());
        stepsDao.setGoal(steps.getGoal());
        stepsDao.setRemarks(steps.getRemarks());
        stepsDao.setValidicRecordID(steps.getValidicRecordID());
        return stepsDao;
    }

    private Steps convertToNormal(StepsBean stepsDAO){
        Steps steps = new Steps(stepsDAO.getCreatedDate());
        steps.setNevoUserID(stepsDAO.getNevoUserID());
        steps.setiD(stepsDAO.getID());
        steps.setDate(stepsDAO.getDate());
        steps.setSteps(stepsDAO.getSteps());
        steps.setWalkSteps(stepsDAO.getWalkSteps());
        steps.setRunSteps(stepsDAO.getRunSteps());
        steps.setDistance(stepsDAO.getDistance());
        steps.setWalkDistance(stepsDAO.getWalkDistance());
        steps.setRunDistance(stepsDAO.getRunDistance());
        steps.setWalkDuration(stepsDAO.getWalkDuration());
        steps.setRunDuration(stepsDAO.getRunDuration());
        steps.setCalories(stepsDAO.getCalories());
        steps.setHourlySteps(stepsDAO.getHourlySteps());
        steps.setHourlyDistance(stepsDAO.getHourlyDistance());
        steps.setHourlyCalories(stepsDAO.getHourlyCalories());
        steps.setInZoneTime(stepsDAO.getInZoneTime());
        steps.setOutZoneTime(stepsDAO.getOutZoneTime());
        steps.setNoActivityTime(stepsDAO.getNoActivityTime());
        steps.setGoal(stepsDAO.getGoal());
        steps.setRemarks(stepsDAO.getRemarks());
        steps.setValidicRecordID(stepsDAO.getValidicRecordID());
        return steps;
    }

    @Override
    public List<Steps> convertToNormalList(List<Optional<Steps>> optionals) {
        List<Steps> stepsList = new ArrayList<>();
        for (Optional<Steps> stepsOptional: optionals) {
            if (stepsOptional.notEmpty()){
                stepsList.add(stepsOptional.get());
            }
        }
        return stepsList;
    }
}
