package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.StepsBean;
import com.dayton.drone.model.Steps;

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

    public StepsDatabaseHelper(Context context) {
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
                    .queryBuilder().where().eq(StepsBean.fUserID, object.getUserID())
                    .and().eq(StepsBean.fDate, object.getDate()).query();
            if(stepsDAOList.isEmpty()) return add(object)!=null;
            StepsBean daoobject = convertToDao(object);
            daoobject.setId(stepsDAOList.get(0).getId());
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
                    .queryBuilder().where().eq(StepsBean.fUserID, userId)
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
                    .queryBuilder().where().eq(StepsBean.fUserID, userId)
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
        stepsDao.setTimeFrame(steps.getTimeFrame());
        stepsDao.setDate(steps.getDate());
        stepsDao.setSteps(steps.getSteps());
        stepsDao.setDistance(steps.getDistance());
        return stepsDao;
    }

    private Steps convertToNormal(StepsBean stepsDAO){
        Steps steps = new Steps();
        steps.setUserID(stepsDAO.getUserID());
        steps.setTimeFrame(stepsDAO.getTimeFrame());
        steps.setDate(stepsDAO.getDate());
        steps.setSteps(stepsDAO.getSteps());
        steps.setDistance(stepsDAO.getDistance());
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
