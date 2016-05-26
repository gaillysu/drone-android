package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.SleepBean;
import com.dayton.drone.model.Sleep;
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

    public SleepDatabaseHelper(Context context) {
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
                    .queryBuilder().where().eq(SleepBean.fUserID, object.getUserID())
                    .and().eq(SleepBean.fDate, object.getDate())
                    .and().eq(SleepBean.fTimeframe, object.getTimeFrame())
                    .query();
            if (sleepDAOList.isEmpty())
                return add(object) != null;
            SleepBean daoobject = convertToDao(object);
            daoobject.setId(sleepDAOList.get(0).getId());
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
                    queryBuilder().where().eq(SleepBean.fUserID, userId).and()
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
                    .eq(SleepBean.fUserID, userId).query();
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
                    .queryBuilder().where().eq(SleepBean.fUserID, userId).and()
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

    private SleepBean convertToDao(Sleep sleep) {
        SleepBean sleepDAO = new SleepBean();
        sleepDAO.setUserID(sleep.getUserID());
        sleepDAO.setTimeFrame(sleep.getTimeFrame());
        sleepDAO.setDate(sleep.getDate());
        sleepDAO.setWakeupTime(sleep.getWakeupTime());
        sleepDAO.setLightSleepTime(sleep.getLightSleepTime());
        sleepDAO.setDeepSleepTime(sleep.getDeepSleepTime());
        sleepDAO.setCloudID(sleep.getCloudID());
        return sleepDAO;
    }

    private Sleep convertToNormal(SleepBean sleepDAO) {
        Sleep sleep = new Sleep();
        sleep.setUserID(sleepDAO.getUserID());
        sleep.setTimeFrame(sleepDAO.getTimeFrame());
        sleep.setWakeupTime(sleepDAO.getWakeupTime());
        sleep.setLightSleepTime(sleepDAO.getLightSleepTime());
        sleep.setDeepSleepTime(sleepDAO.getDeepSleepTime());
        sleep.setCloudID(sleepDAO.getCloudID());
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
