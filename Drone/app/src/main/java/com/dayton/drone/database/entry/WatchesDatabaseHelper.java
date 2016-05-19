package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.WatchesBean;
import com.dayton.drone.model.Watches;

import net.medcorp.library.ble.util.Optional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by med on 16/5/19.
 */
public class WatchesDatabaseHelper {
    private DatabaseHelper databaseHelper;

    public WatchesDatabaseHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelperInstance(context);
        }
    }

    public Optional<Watches> add(Watches object) {
        Optional<Watches> watchesOptional = new Optional<>();
        try {
            int res = databaseHelper.getWatchesBean().create(convertToBean(object));
            if(res > 0)
            {
                watchesOptional.set(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return watchesOptional;
    }

    public boolean update(Watches object) {
        int result = -1;
        try {
            List<WatchesBean> list = databaseHelper.getWatchesBean().queryBuilder()
                    .where().eq(Watches.fMacAddress, object.getMacAddress()).query();
            if (list.isEmpty()) {
                return add(object) != null;
            }
            WatchesBean bean = convertToBean(object);
            bean.setId(list.get(0).getId());
            result = databaseHelper.getWatchesBean().update(bean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result >= 0;
    }


    public boolean remove(Watches object) {
        try {
            List<WatchesBean> list = databaseHelper.getWatchesBean().queryBuilder()
                    .where().eq(Watches.fMacAddress, object.getMacAddress()).and().eq(Watches.fUserID, object.getUserID()).query();
            if (!list.isEmpty())
            {
                return databaseHelper.getWatchesBean().delete(list)>=0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Watches> getAll(String userId) {
        List<Watches> watchesList = new ArrayList<>();
        try {
            List<WatchesBean> list = databaseHelper.getWatchesBean().queryBuilder()
                    .where().eq(Watches.fUserID, userId).query();
            for(WatchesBean watchesBean:list)
            {
                watchesList.add(convertToNormal(watchesBean));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return watchesList;
    }

    private Watches convertToNormal(WatchesBean watchesBean)
    {
        Watches watches = new Watches();
        watches.setWatchID(watchesBean.getWatchID());
        watches.setUserID(watchesBean.getUserID());
        watches.setSerialNumber(watchesBean.getSerialNumber());
        watches.setMacAddress(watchesBean.getMacAddress());
        watches.setModelName(watchesBean.getModelName());
        watches.setFirmwareVersion(watchesBean.getFirmwareVersion());
        return watches;
    }

    private WatchesBean convertToBean(Watches watches)
    {
        WatchesBean watchesBean = new WatchesBean();
        watchesBean.setWatchID(watches.getWatchID());
        watchesBean.setUserID(watches.getUserID());
        watchesBean.setSerialNumber(watches.getSerialNumber());
        watchesBean.setMacAddress(watches.getMacAddress());
        watchesBean.setModelName(watches.getModelName());
        watchesBean.setFirmwareVersion(watches.getFirmwareVersion());
        return watchesBean;
    }
}
