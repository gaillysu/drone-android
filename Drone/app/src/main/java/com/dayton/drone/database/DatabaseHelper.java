package com.dayton.drone.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.database.bean.AlarmBean;
import com.dayton.drone.database.bean.SleepBean;
import com.dayton.drone.database.bean.StepsBean;
import com.dayton.drone.database.bean.UserBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by boy on 2016/4/12.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "droneWatch";
    private static final int DATABASE_VERSION = 1;

    private Dao<UserBean, Integer> userBean = null;
    private Dao<AlarmBean,Integer> alarmBean = null;
    private Dao<StepsBean,Integer> stepsBean = null;
    private Dao<SleepBean,Integer> sleepBean = null;
    public static DatabaseHelper instance;

    public static synchronized DatabaseHelper getHelperInstance(Context context) {
        context = ApplicationModel.getContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }
        return instance;
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {

            TableUtils.createTable(connectionSource, UserBean.class);
            TableUtils.createTable(connectionSource,AlarmBean.class);
            TableUtils.createTable(connectionSource,StepsBean.class);
            TableUtils.createTable(connectionSource,SleepBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1)
    {

    }

    public Dao<UserBean, Integer> getUserBean() throws SQLException {
        if(userBean == null){
            userBean = getDao(UserBean.class);
        }
        return  userBean;
    }

    public Dao<AlarmBean,Integer> getAlarmBean() throws SQLException {
        if(alarmBean== null){
                alarmBean = getDao(AlarmBean.class);
        }
        return alarmBean;
    }


    public Dao<StepsBean,Integer> getStepsBean() throws SQLException {
        if(stepsBean== null){
            stepsBean = getDao(StepsBean.class);
        }
        return stepsBean;
    }

    public Dao<SleepBean,Integer> getSleepBean() throws SQLException {
        if(sleepBean== null){
            sleepBean = getDao(SleepBean.class);
        }
        return sleepBean;
    }
}
