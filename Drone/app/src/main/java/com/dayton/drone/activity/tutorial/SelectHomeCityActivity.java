package com.dayton.drone.activity.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.ChooseCityActivity;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.event.CityNumberChangedEvent;
import com.dayton.drone.event.WorldClockChangedEvent;
import com.dayton.drone.utils.SpUtils;

import net.medcorp.library.worldclock.City;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SelectHomeCityActivity extends BaseActivity {

    @Bind(R.id.activity_select_home_city_textview)
    TextView  homeCity;
    @Bind(R.id.activity_select_home_time_textview)
    TextView  homeTime;

    @Bind(R.id.calibrate_next_page_button)
    Button calibrateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_home_city);
        ButterKnife.bind(this);
        int homeCityId = SpUtils.getHomeCityId(SelectHomeCityActivity.this);
        if(homeCityId !=-1)
        {
            City city = getModel().getWorldClockDatabaseHelper().get(homeCityId);
            homeCity.setText(city.getName());
            homeTime.setText(obtainCityTime(city));
            enableNextButton(true);
        }
        else {
            enableNextButton(false);
        }
    }

    private void enableNextButton(boolean enable) {
        if(enable)
        {
            calibrateButton.setTextColor(getResources().getColor(android.R.color.white));
        }
        else {
            calibrateButton.setTextColor(getResources().getColor(R.color.disable_gray_color));
        }
        calibrateButton.setEnabled(enable);
    }

    @OnClick(R.id.calibrate_next_page_button)
    public void calibrateWatch()
    {
        Intent intent  = new Intent(SelectHomeCityActivity.this ,CalibrateWatchHourActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.activity_select_home_city_layout)
    public void selectHomeCity()
    {
        Intent intent = new Intent(SelectHomeCityActivity.this, ChooseCityActivity.class);
        intent.putExtra(getString(R.string.is_select_home_city), true);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                boolean flag = data.getBooleanExtra(getString(R.string.is_choose_flag), false);
                if (flag) {
                    int newCityId = data.getIntExtra(getString(R.string.select_city_id),-1);
                    int oldCityId = SpUtils.getHomeCityId(SelectHomeCityActivity.this);
                    if(oldCityId!=newCityId)
                    {
                        SpUtils.saveHomeCityId(SelectHomeCityActivity.this, newCityId);
                        City city = getModel().getWorldClockDatabaseHelper().get(newCityId);
                        homeCity.setText(city.getName());
                        homeTime.setText(obtainCityTime(city));
                        enableNextButton(true);
                        if(oldCityId!=-1)
                        {
                            city = getModel().getWorldClockDatabaseHelper().get(oldCityId);
                            city.setSelected(false);
                            getModel().getWorldClockDatabaseHelper().update(city);
                        }
                        EventBus.getDefault().post(new CityNumberChangedEvent());
                        EventBus.getDefault().post(new WorldClockChangedEvent());
                    }
                }
            }
        }
    }

    private String obtainCityTime(City city) {
        String gmtString = "GMT+00:00";
        //getOffSetFromGMT return gmt offset "in minutes"
        if(city.getOffSetFromGMT()>=0) {
            gmtString = String.format("GMT+%02d:%02d",city.getOffSetFromGMT()/60,city.getOffSetFromGMT()%60);
        }
        else {
            gmtString = String.format("GMT-%02d:%02d",(-1*city.getOffSetFromGMT())/60,(-1*city.getOffSetFromGMT())%60);
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(gmtString));
        String am_pm = calendar.get(Calendar.HOUR_OF_DAY) <= 12 ?
                getModel().getString(R.string.world_clock_am) : getModel().getString(R.string.world_clock_pm);
        String minute = calendar.get(Calendar.MINUTE) >= 10 ? calendar.get(Calendar.MINUTE) + "" : "0" + calendar.get(Calendar.MINUTE);
        int hour = SpUtils.get24HourFormat(getModel())?calendar.get(Calendar.HOUR_OF_DAY):calendar.get(Calendar.HOUR);
        return hour + ":" + minute + (SpUtils.get24HourFormat(getModel())?"":am_pm);
    }
}
