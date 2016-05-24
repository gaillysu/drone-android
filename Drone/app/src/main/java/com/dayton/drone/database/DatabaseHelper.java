package com.dayton.drone.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.dayton.drone.database.bean.NotificationBean;
import com.dayton.drone.database.bean.SleepBean;
import com.dayton.drone.database.bean.StepsBean;
import com.dayton.drone.database.bean.UserBean;
import com.dayton.drone.database.bean.WatchesBean;
import com.dayton.drone.database.bean.WorldClockBean;
import com.dayton.drone.model.WorldClock;
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
    /**
     * change history
     * v1: create
     * v2: add "password" field in User table
     * v3: add "watches" table
     * v4: modify world clock table struct
     */
    private static final int DATABASE_VERSION = 4;

    private Dao<UserBean, Integer> userBean = null;
    private Dao<StepsBean,Integer> stepsBean = null;
    private Dao<SleepBean,Integer> sleepBean = null;
    private Dao<NotificationBean,Integer> notificationBean = null;
    private Dao<WorldClockBean,Integer> worldClockBean = null;
    private Dao<WatchesBean,Integer> watchesBean = null;
    public static DatabaseHelper instance;

    public static synchronized DatabaseHelper getHelperInstance(Context context) {
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
            TableUtils.createTable(connectionSource,StepsBean.class);
            TableUtils.createTable(connectionSource,SleepBean.class);
            TableUtils.createTable(connectionSource,NotificationBean.class);
            TableUtils.createTable(connectionSource,WorldClockBean.class);
            TableUtils.createTable(connectionSource,WatchesBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion)
    {
        try {
            TableUtils.dropTable(connectionSource,UserBean.class,true);
            TableUtils.dropTable(connectionSource, StepsBean.class, true);
            TableUtils.dropTable(connectionSource, SleepBean.class, true);
            TableUtils.dropTable(connectionSource, NotificationBean.class, true);
            TableUtils.dropTable(connectionSource, WorldClockBean.class, true);
            TableUtils.dropTable(connectionSource, WatchesBean.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<UserBean, Integer> getUserBean() throws SQLException {
        if(userBean == null){
            userBean = getDao(UserBean.class);
        }
        return  userBean;
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
    public Dao<NotificationBean,Integer> getNotificationBean() throws SQLException {
        if(notificationBean== null){
            notificationBean = getDao(NotificationBean.class);
        }
        return notificationBean;
    }

    public Dao<WorldClockBean,Integer> getWorldClockBean() throws SQLException {
        if(worldClockBean== null){
            worldClockBean = getDao(WorldClockBean.class);
        }
        return worldClockBean;
    }

    public Dao<WatchesBean,Integer> getWatchesBean() throws SQLException {
        if(watchesBean == null){
            watchesBean = getDao(WatchesBean.class);
        }
        return watchesBean;
    }
}
