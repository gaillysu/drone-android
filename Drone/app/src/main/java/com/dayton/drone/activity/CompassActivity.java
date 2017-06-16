package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.SpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by med on 17/5/4.
 */

public class CompassActivity extends BaseActivity {

    private boolean enableCompass;
    private int compassAutoOnDuration;
    private int compassScreenTimeout;

    @Bind(R.id.activity_compass_auto_on_duration_textview)
    TextView compassAutoOnDurationTextView;
    @Bind(R.id.activity_compass_screen_timeout_textview)
    TextView compassScreenTimeoutTextView;

    @Bind(R.id.activity_compass_enable_switch)
    SwitchCompat compassEnableSwitch;
    @Bind(R.id.my_toolbar)
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        ButterKnife.bind(this);
        initToolbar();
        enableCompass = SpUtils.getBoolean(this,CacheConstants.ENABLE_COMPASS,false);
        compassAutoOnDuration = SpUtils.getIntMethod(this, CacheConstants.COMPASS_AUTO_ON_DURATION,CacheConstants.COMPASS_AUTO_ON_DURATION_DEFAULT);
        compassScreenTimeout = SpUtils.getIntMethod(this, CacheConstants.COMPASS_SCREEN_TIMEOUT,CacheConstants.COMPASS_SCREEN_TIMEOUT_DEFAULT);
        compassEnableSwitch.setChecked(enableCompass);
        compassAutoOnDurationTextView.setText(formatMinutes(compassAutoOnDuration +""));
        compassScreenTimeoutTextView.setText(formatSeconds(compassScreenTimeout +""));
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        TextView title = (TextView) mToolbar.findViewById(R.id.toolbar_title_tv);
        title.setText(getString(R.string.compass_title));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.activity_compass_setting_layout)
    public void editAutoOffTime(){
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(CompassActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {
                        compassAutoOnDurationTextView.setText(formatMinutes(dateDesc));
                        int durationMinutes = new Integer(dateDesc).intValue();
                        compassAutoOnDuration = durationMinutes;
                        SpUtils.putIntMethod(CompassActivity.this,CacheConstants.COMPASS_AUTO_ON_DURATION,durationMinutes);
                        getModel().getSyncController().setCompassAutoOnDuration(durationMinutes);
                    }
                }).viewStyle(4)
                .viewTextSize(20)
                .dateChose(""+ compassAutoOnDuration)
                .build();
        pickerPopWin.showPopWin(CompassActivity.this);
    }

    @OnClick(R.id.activity_compass_screen_timeout_setting_layout)
    public void editScreenTimeout(){
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(CompassActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {
                        compassScreenTimeoutTextView.setText(formatSeconds(dateDesc));
                        int screenTimeoutInSeconds = new Integer(dateDesc).intValue();
                        compassScreenTimeout = screenTimeoutInSeconds;
                        SpUtils.putIntMethod(CompassActivity.this,CacheConstants.COMPASS_SCREEN_TIMEOUT,screenTimeoutInSeconds);
                        getModel().getSyncController().setCompassTimeout(screenTimeoutInSeconds);
                    }
                }).viewStyle(6)
                .viewTextSize(20)
                .dateChose(""+ compassScreenTimeout)
                .build();
        pickerPopWin.showPopWin(CompassActivity.this);
    }

    @OnClick(R.id.activity_compass_start_compass_calibration_layout)
    public void startCompassCalibration(){
        startActivity(new Intent(CompassActivity.this ,CalibrateCompassActivity.class));
    }

    @OnCheckedChanged(R.id.activity_compass_enable_switch)
    public void enableCompass(CompoundButton buttonView, boolean isChecked){
        SpUtils.putBoolean(this,CacheConstants.ENABLE_COMPASS,isChecked);
        getModel().getSyncController().enableCompass(isChecked);
    }
    private String formatMinutes(String minutes){
        int theMinute = new Integer(minutes).intValue();
        if(theMinute%60==0) {
            return theMinute/60 + "h";
        }
        else {
           if(theMinute<60) {
               return theMinute + "mins";
           }
           else {
               return theMinute/60 + "h " + theMinute%60 + "m";
           }
        }
    }

    private String formatSeconds(String seconds){
        int theSecond = new Integer(seconds).intValue();
        if(theSecond%60==0) {
            return theSecond/60 + "mins";
        }
        else {
            if(theSecond<60) {
                return theSecond + "secs";
            }
            else {
                return theSecond/60 + "m " + theSecond%60 + "s";
            }
        }
    }
}
