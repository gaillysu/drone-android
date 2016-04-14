package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.SleepBean;
import com.dayton.drone.modle.Sleep;
import net.medcorp.library.ble.util.Optional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by karl-john on 17/11/15.
 */
public class SleepDatabaseHelper implements iEntryDatabaseHelper<Sleep> {

    private DatabaseHelper databaseHelper;

    private SleepDatabaseHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelperInstance(context);
        }
    }

    @Override
    public Optional<Sleep> add(Sleep object) {
        Optional<Sleep> sleepOptional = new Optional<>();
        try {
            SleepBean res = databaseHelper.getSleepBean().createIfNotExists(convertToDao(object));
            if (res != null) {
                sleepOptional.set(convertToNormal(res));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sleepOptional;
    }

    @Override
    public boolean update(Sleep object) {
        int result = -1;
        try {
            List<SleepBean> sleepDAOList = databaseHelper.getSleepBean()
                    .queryBuilder().where().eq(SleepBean.fNevoUserID, object.getNevoUserID())
                    .and().eq(SleepBean.fDate, object.getDate()).query();
            if (sleepDAOList.isEmpty())
                return add(object) != null;
            SleepBean daoobject = convertToDao(object);
            daoobject.setID(sleepDAOList.get(0).getID());
            result = databaseHelper.getSleepBean().update(daoobject);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result >= 0;
    }

    @Override
    public boolean remove(String userId, Date date) {
        try {
            List<SleepBean> sleepDAOList = databaseHelper.getSleepBean().
                    queryBuilder().where().eq(SleepBean.fNevoUserID, userId).and()
                    .eq(SleepBean.fDate, date.getTime()).query();
            if (!sleepDAOList.isEmpty()) {
                return databaseHelper.getSleepBean().delete(sleepDAOList) >= 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Optional<Sleep>> get(String userId) {
        List<Optional<Sleep>> sleepList = new ArrayList<Optional<Sleep>>();
        try {
            List<SleepBean> sleepDAOList = databaseHelper.getSleepBean()
                    .queryBuilder().orderBy(SleepBean.fDate, false).where()
                    .eq(SleepBean.fNevoUserID, userId).query();
            for (SleepBean sleepDAO : sleepDAOList) {
                Optional<Sleep> sleepOptional = new Optional<Sleep>();
                sleepOptional.set(convertToNormal(sleepDAO));
                sleepList.add(sleepOptional);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sleepList;
    }

    @Override
    public Optional<Sleep> get(String userId, Date date) {
        List<Optional<Sleep>> sleepList = new ArrayList<Optional<Sleep>>();
        try {
            List<SleepBean> sleepDAOList = databaseHelper.getSleepBean()
                    .queryBuilder().where().eq(SleepBean.fNevoUserID, userId).and()
                    .eq(SleepBean.fDate, date.getTime()).query();
            for (SleepBean sleepDAO : sleepDAOList) {
                Optional<Sleep> sleepOptional = new Optional<Sleep>();
                sleepOptional.set(convertToNormal(sleepDAO));
                sleepList.add(sleepOptional);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sleepList.isEmpty() ? new Optional<Sleep>() : sleepList.get(0);
    }

    @Override
    public List<Optional<Sleep>> getAll(String userId) {
        return get(userId);
    }

    public List<Sleep> getNeedSyncSleep(String userId) {
        List<Sleep> sleepList = new ArrayList<Sleep>();
        try {
            List<SleepBean> sleepDAOList = databaseHelper.getSleepBean().
                    queryBuilder().orderBy(SleepBean.fDate, false).where()
                    .eq(SleepBean.fNevoUserID, userId).and().eq(SleepBean.fValidicRecordID, "0").query();
            for (SleepBean sleepDAO : sleepDAOList) {
                sleepList.add(convertToNormal(sleepDAO));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sleepList;
    }

    public boolean isFoundInLocalSleep(int activity_id) {
        try {
            List<SleepBean> sleepDAOList = databaseHelper.getSleepBean().
                    queryBuilder().where().eq(SleepBean.fID, activity_id).query();
            return !sleepDAOList.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private SleepBean convertToDao(Sleep sleep) {
        SleepBean sleepDAO = new SleepBean();
        sleepDAO.setID(sleep.getiD());
        sleepDAO.setNevoUserID(sleep.getNevoUserID());
        sleepDAO.setCreatedDate(sleep.getCreatedDate());
        sleepDAO.setDate(sleep.getDate());
        sleepDAO.setEnd(sleep.getEnd());
        sleepDAO.setHourlyDeep(sleep.getHourlyDeep());
        sleepDAO.setHourlyLight(sleep.getHourlyLight());
        sleepDAO.setHourlySleep(sleep.getHourlySleep());
        sleepDAO.setHourlyWake(sleep.getHourlyWake());
        sleepDAO.setRemarks(sleep.getRemarks());
        sleepDAO.setSleepQuality(sleep.getSleepQuality());
        sleepDAO.setStart(sleep.getStart());
        sleepDAO.setTotalDeepTime(sleep.getTotalDeepTime());
        sleepDAO.setTotalLightTime(sleep.getTotalLightTime());
        sleepDAO.setTotalSleepTime(sleep.getTotalSleepTime());
        sleepDAO.setTotalWakeTime(sleep.getTotalWakeTime());
        sleepDAO.setValidicRecordID(sleep.getValidicRecordID());
        return sleepDAO;
    }

    private Sleep convertToNormal(SleepBean sleepDAO) {
        Sleep sleep = new Sleep(sleepDAO.getCreatedDate());
        sleep.setNevoUserID(sleepDAO.getNevoUserID());
        sleep.setiD(sleepDAO.getID());
        sleep.setDate(sleepDAO.getDate());
        sleep.setEnd(sleepDAO.getEnd());
        sleep.setHourlyDeep(sleepDAO.getHourlyDeep());
        sleep.setHourlyLight(sleepDAO.getHourlyLight());
        sleep.setHourlySleep(sleepDAO.getHourlySleep());
        sleep.setHourlyWake(sleepDAO.getHourlyWake());
        sleep.setRemarks(sleepDAO.getRemarks());
        sleep.setSleepQuality(sleepDAO.getSleepQuality());
        sleep.setStart(sleepDAO.getStart());
        sleep.setTotalDeepTime(sleepDAO.getTotalDeepTime());
        sleep.setTotalLightTime(sleepDAO.getTotalLightTime());
        sleep.setTotalSleepTime(sleepDAO.getTotalSleepTime());
        sleep.setTotalWakeTime(sleepDAO.getTotalWakeTime());
        sleep.setValidicRecordID(sleepDAO.getValidicRecordID());
        return sleep;
    }

    @Override
    public List<Sleep> convertToNormalList(List<Optional<Sleep>> optionals) {
        List<Sleep> sleepList = new ArrayList<>();
        for (Optional<Sleep> sleepOptional : optionals) {
            if (sleepOptional.notEmpty()) {
                sleepList.add(sleepOptional.get());
            }
        }
        return sleepList;
    }
}