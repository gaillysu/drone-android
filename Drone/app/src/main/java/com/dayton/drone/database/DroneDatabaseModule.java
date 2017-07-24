package com.dayton.drone.database;

import com.dayton.drone.database.bean.AlarmBean;
import com.dayton.drone.database.bean.CityWeatherBean;
import com.dayton.drone.database.bean.HourlyForecastBean;
import com.dayton.drone.database.bean.NotificationBean;
import com.dayton.drone.database.bean.StepsBean;
import com.dayton.drone.database.bean.UserBean;
import com.dayton.drone.database.bean.WatchesBean;

import io.realm.annotations.RealmModule;

/**
 * Created by med on 17/3/28.
 */
@RealmModule(classes = {NotificationBean.class, StepsBean.class, UserBean.class, WatchesBean.class, AlarmBean.class, CityWeatherBean.class, HourlyForecastBean.class})
public class DroneDatabaseModule {
}
