package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.bean.CityWeatherBean;
import com.dayton.drone.database.bean.HourlyForecastBean;
import com.dayton.drone.network.response.model.HourlyData;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by med on 17/7/24.
 */

public class CityWeatherDatabaseHelper {
    private Realm realm;
    private final String CITYNAME = "cityName";

    public CityWeatherDatabaseHelper(Context context) {
        realm = Realm.getDefaultInstance();
    }
    public void addOrUpdate(final String cityName, final HourlyData[] hourlyDatas)
    {
        realm.beginTransaction();
        CityWeatherBean cityWeatherBean = realm.where(CityWeatherBean.class).equalTo(CITYNAME, cityName).findFirst();
        if(cityWeatherBean==null) {
            cityWeatherBean = realm.createObject(CityWeatherBean.class);
        }
        else {
            cityWeatherBean.getWeatherData().clear();
        }
        cityWeatherBean.setCityName(cityName);
        for(HourlyData hourlyData:hourlyDatas)
        {
            HourlyForecastBean hourlyForecastBean = new HourlyForecastBean();
            hourlyForecastBean.setTime(hourlyData.getTime());
            hourlyForecastBean.setTemperature(hourlyData.getTemperature());
            hourlyForecastBean.setIcon(hourlyData.getIcon());
            hourlyForecastBean.setSummary(hourlyData.getSummary());
            cityWeatherBean.getWeatherData().add(realm.copyToRealm(hourlyForecastBean));
        }
        realm.commitTransaction();

    }

    public void remove(final String cityName) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CityWeatherBean cityWeatherBean = realm.where(CityWeatherBean.class).equalTo(CITYNAME, cityName).findFirst();
                if(cityWeatherBean!=null) {
                    cityWeatherBean.deleteFromRealm();
                }
            }
        });
    }

    public List<HourlyForecastBean> get(final String cityName) {
        CityWeatherBean cityWeatherBean = realm.where(CityWeatherBean.class).equalTo(CITYNAME, cityName).findFirst();
        if(cityWeatherBean!=null) {
            return cityWeatherBean.getWeatherData();
        }
        return new ArrayList<>();
    }

}

