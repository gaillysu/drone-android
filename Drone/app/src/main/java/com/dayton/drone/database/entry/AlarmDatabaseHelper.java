package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.AlarmBean;
import com.dayton.drone.modle.Alarm;

import net.medcorp.library.ble.util.Optional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by boy on 2016/4/13.
 * Alarm database table
 */
public class AlarmDatabaseHelper implements DatabaseWieldMode<Alarm> {

    DatabaseHelper databaseHelper;

    public AlarmDatabaseHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelperInstance(context);
        }
    }

    @Override
    public Optional<Alarm> add(Alarm Object) {
        Optional<Alarm> optional = new Optional<>();
        try {
            int result = databaseHelper.getAlarmBean().create(convertToBean(Object));
            if (result > 0) {
                optional.set(Object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optional;
    }

    @Override
    public boolean upData(Alarm Object) {
        int result = -1;
        try {
            List<AlarmBean> list = databaseHelper.getAlarmBean().queryBuilder().where()
                    .eq(AlarmBean.iDString, Object.getId()).query();
            if(list != null){
                return add(Object) != null;
            }

            AlarmBean alarmBean = convertToBean(Object);
            alarmBean.setID(list.get(0).getID());
            result = list.get(0).getID();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result>=0;
    }

    @Override
    public boolean remove(int rid) {
        try {
            List<AlarmBean> alarmDAOList = databaseHelper.getAlarmBean().queryBuilder().where().eq(AlarmBean.iDString, rid).query();
            if(!alarmDAOList.isEmpty())
            {
                return databaseHelper.getAlarmBean().delete(alarmDAOList)>=0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public List<Optional<Alarm>> get(int rid) {
        List<Optional<Alarm>> alarmList = new ArrayList<>();
        try {
            List<AlarmBean> alarmBeanList = databaseHelper.getAlarmBean().queryBuilder().where().eq(AlarmBean.iDString, rid).query();
            for(AlarmBean alarmBean: alarmBeanList) {
                Alarm alarm = convertToNormal(alarmBean);;
                Optional<Alarm> alarmOptional = new Optional<>();
                alarmOptional.set(alarm);
                alarmList.add(alarmOptional);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alarmList;
    }

    @Override
    public List<Optional<Alarm>> getAll() {
        List<Optional<Alarm>> alarmList = new ArrayList<>();
        try {
            List<AlarmBean> alarmDAOList  = databaseHelper.getAlarmBean().queryBuilder().query();
            for(AlarmBean alarmBean: alarmDAOList) {
                Optional alarm = new Optional<Alarm>();
                alarm.set(convertToNormal(alarmBean));
                alarmList.add(alarm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alarmList;
    }

    @Override
    public List<Alarm> convertToNormalList(List<Optional<Alarm>> optionals) {
        return null;
    }


    private AlarmBean convertToBean(Alarm object) {

        AlarmBean alarmBean = new AlarmBean();
        alarmBean.setAlarm(object.getHour() + ":" + object.getMinute());
        alarmBean.setLabel(object.getLabel());
        alarmBean.setEnabled(object.isEnable());
        return alarmBean;
    }

    private Alarm convertToNormal(AlarmBean bean){
        String[] splittedAlarmStrings = bean.getAlarm().split(":");
        int hour = Integer.parseInt(splittedAlarmStrings[0]);
        int minutes = Integer.parseInt(splittedAlarmStrings[1]);
        Alarm alarm = new Alarm(hour,minutes,bean.isEnabled(),bean.getLabel());
        alarm.setId(bean.getID());
        return  alarm;
    }

}

