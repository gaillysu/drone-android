package com.dayton.drone.activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.utils.SpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by med on 17/5/4.
 */

public class CompassActivity extends BaseActivity {

    private final String COMPASS_AUTO_OFF_TIME = "compass_auto_off_time";
    private final String ENABLE_COMPASS = "enable_compass";
    private boolean enableCompass;
    private int autoOffMinutes;

    @Bind(R.id.activity_compass_auto_off_minute_textview)
    TextView autoOffMinuteTextView;

    @Bind(R.id.activity_compass_enable_switch)
    SwitchCompat compassEnableSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        ButterKnife.bind(this);
        enableCompass = SpUtils.getBoolean(this,ENABLE_COMPASS,false);
        autoOffMinutes = SpUtils.getIntMethod(this,COMPASS_AUTO_OFF_TIME,15);
        compassEnableSwitch.setChecked(enableCompass);
        autoOffMinuteTextView.setText(formatMinutes(autoOffMinutes+""));
    }

    @OnClick(R.id.activity_compass_back_imagebutton)
    public void back2MainMenu(){
        finish();
    }

    @OnClick(R.id.activity_compass_setting_layout)
    public void editAutoOffTime(){
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(CompassActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {
                        autoOffMinuteTextView.setText(formatMinutes(dateDesc));
                        SpUtils.putIntMethod(CompassActivity.this,COMPASS_AUTO_OFF_TIME,new Integer(dateDesc).intValue());
                        //TODO send config commpass command
                    }
                }).viewStyle(4)
                .viewTextSize(20)
                .dateChose(""+autoOffMinutes)
                .build();
        pickerPopWin.showPopWin(CompassActivity.this);
    }

    @OnCheckedChanged(R.id.activity_compass_enable_switch)
    public void enableCompass(CompoundButton buttonView, boolean isChecked){
        SpUtils.putBoolean(this,ENABLE_COMPASS,isChecked);
        //TODO send config commpass command

    }
    private String formatMinutes(String minutes){
        int theMinute = new Integer(minutes).intValue();
        if(theMinute%60==0) {
            return theMinute/60 + "h";
        }
        else {
           if(theMinute<60) {
               return theMinute + "min";
           }
           else {
               return theMinute/60 + "h " + theMinute%60 + "min";
           }
        }
    }
}
