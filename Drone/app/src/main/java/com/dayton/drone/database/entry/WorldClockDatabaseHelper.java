package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.WorldClockBean;
import com.dayton.drone.model.WorldClock;

import net.medcorp.library.ble.util.Optional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by med on 16/5/9.
 */
public class WorldClockDatabaseHelper {
    private DatabaseHelper databaseHelper;

    public WorldClockDatabaseHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelperInstance(context);
        }
        initDatabase();
    }


    public void initDatabase() {
        //total 581 timezone is retrieved, set 4 default timezone
        if(getAll().isEmpty()) {
            String[] iDs = TimeZone.getAvailableIDs();
            for(String id:iDs) {
                WorldClock worldClock = new WorldClock();
                if(id.contains("Shanghai")
                        ||id.contains("London")
                        ||id.contains("Paris")
                        ||id.contains("New_York"))
                {
                    worldClock.setSelected(1);
                }
                worldClock.setTimeZoneName(id);
                add(worldClock);
            }

            List<WorldClock> defaults = getSelected();
            int len = defaults.size();
        }
    }

    public Optional<WorldClock> add(WorldClock object) {
        Optional<WorldClock> userOptional = new Optional<>();
        try {
            int res = databaseHelper.getWorldClockBean().create(convertToBean(object));
            if (res > 0) {
                userOptional.set(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userOptional;
    }

    public boolean update(WorldClock object) {
        int result = -1;
        try {
            List<WorldClockBean> list = databaseHelper.getWorldClockBean().queryBuilder()
                    .where().eq(WorldClock.fTimeZoneName, object.getTimeZoneName()).query();
            if (list.isEmpty()) {
                return add(object) != null;
            }
            WorldClockBean bean = convertToBean(object);
            bean.setId(list.get(0).getId());
            result = databaseHelper.getWorldClockBean().update(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result >= 0;
    }


    public List<WorldClock> getSelected() {
        List<WorldClock> list = new ArrayList<>();
        try {
            List<WorldClockBean> worldClockBeanList = databaseHelper.getWorldClockBean().queryBuilder()
                    .where().eq(WorldClock.fSelected,1).query();
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
        return worldClock;
    }

    private WorldClockBean convertToBean(WorldClock worldClock) {
        WorldClockBean worldClockBean = new WorldClockBean();
        worldClockBean.setTimeZoneName(worldClock.getTimeZoneName());
        worldClockBean.setSelected(worldClock.getSelected());
        return worldClockBean;
    }

}
