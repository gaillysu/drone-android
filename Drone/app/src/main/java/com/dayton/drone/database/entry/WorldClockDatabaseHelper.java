package com.dayton.drone.database.entry;

import android.content.Context;
import android.util.Log;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.WorldClockBean;
import com.dayton.drone.model.WorldClock;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by med on 16/5/9.
 */
public class WorldClockDatabaseHelper {
    private DatabaseHelper databaseHelper;

    public WorldClockDatabaseHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelperInstance(context);
        }
        initDatabase(context);
    }

    private void initDatabase(Context context) {
        if(getAll().isEmpty())
        {
            try {
                String[] files = context.getAssets().list("localtimezone");
                for(String file:files)
                {
                    if(file.equals("timezone.txt")) {
                        InputStream is = context.getAssets().open("localtimezone/"+file);
                        JsonReader reader = new JsonReader(new InputStreamReader(is));
                        try {
                            String groupName;
                            String timeZoneName;
                            String timeZoneTitle;
                            reader.beginObject();
                            while(reader.hasNext()) {
                                groupName = reader.nextName();
                                reader.beginObject();
                                while(reader.hasNext())
                                {
                                    timeZoneTitle = reader.nextName();
                                    timeZoneName = reader.nextString();
                                    WorldClock worldClock = new WorldClock();
                                    worldClock.setTimeZoneCategory(groupName);
                                    worldClock.setTimeZoneName(timeZoneName);
                                    worldClock.setTimeZoneTitle(timeZoneTitle);
                                    worldClock.setSelected(0);
                                    add(worldClock);
                                    Log.i("Gailly", groupName +": " + timeZoneName + ": " + timeZoneTitle);
                                }
                                reader.endObject();
                            }
                            reader.endObject();
                        } finally {
                            reader.close();
                        }
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                /** //if has not json file
                String[] iDs = TimeZone.getAvailableIDs();
                for(String id:iDs) {
                    WorldClock worldClock = new WorldClock();
                    worldClock.setTimeZoneName(id);
                    TimeZone timeZone = TimeZone.getTimeZone(id);
                    worldClock.setTimeZoneOffset(timeZone.getRawOffset()/1000.f/3600.f);
                    add(worldClock);
                }
                 */
            }
        }
    }



    /**
     * add function , only used by class inner function
     * @param object
     * @return
     */
    private boolean add(WorldClock object) {
        try {
            int res = databaseHelper.getWorldClockBean().create(convertToBean(object));
            if (res > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(WorldClock object,boolean selected) {
        try {
            List<WorldClockBean> list = databaseHelper.getWorldClockBean().queryBuilder()
                    .where().eq(WorldClockBean.fTimeZoneName, object.getTimeZoneName()).query();

            if (list.isEmpty())
            {
                return false;
            }
            WorldClockBean worldClockBean = list.get(0);
            if(worldClockBean.getSelected() == 1 && selected) {
                return false;
            }
            worldClockBean.setSelected(selected?1:0);
            int res = databaseHelper.getWorldClockBean().update(worldClockBean);
            if (res > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<WorldClock> getSelected() {
        List<WorldClock> list = new ArrayList<>();
        try {
            List<WorldClockBean> worldClockBeanList = databaseHelper.getWorldClockBean().queryBuilder()
                    .where().eq(WorldClockBean.fSelected,1).query();
            for(WorldClockBean worldClockBean : worldClockBeanList)
            {
                list.add(convertToNormal(worldClockBean));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<WorldClock> getAll() {
        List<WorldClock> list = new ArrayList<>();
        try {
            List<WorldClockBean> worldClockBeanList = databaseHelper.getWorldClockBean().queryBuilder().query();
            for(WorldClockBean worldClockBean : worldClockBeanList)
            {
                list.add(convertToNormal(worldClockBean));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private WorldClock convertToNormal(WorldClockBean worldClockBean) {
        WorldClock worldClock = new WorldClock();
        worldClock.setTimeZoneName(worldClockBean.getTimeZoneName());
        worldClock.setSelected(worldClockBean.getSelected());
        worldClock.setTimeZoneCategory(worldClockBean.getTimeZoneCategory());
        worldClock.setTimeZoneTitle(worldClockBean.getTimeZoneTitle());
        return worldClock;
    }

    private WorldClockBean convertToBean(WorldClock worldClock) {
        WorldClockBean worldClockBean = new WorldClockBean();
        worldClockBean.setTimeZoneName(worldClock.getTimeZoneName());
        worldClockBean.setSelected(worldClock.getSelected());
        worldClockBean.setTimeZoneCategory(worldClock.getTimeZoneCategory());
        worldClockBean.setTimeZoneTitle(worldClock.getTimeZoneTitle());
        return worldClockBean;
    }

}
