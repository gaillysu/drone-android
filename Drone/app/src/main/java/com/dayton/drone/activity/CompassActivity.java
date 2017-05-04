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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * Created by med on 17/5/4.
 */

public class CompassActivity extends BaseActivity {

    @Bind(R.id.activity_compass_auto_off_minute_textview)
    TextView autoOffMinute;

    @Bind(R.id.activity_compass_enable_switch)
    SwitchCompat enableCompass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        ButterKnife.bind(this);
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
                        autoOffMinute.setText(formatMinutes(dateDesc));
                        //TODO send config commpass command
                    }
                }).viewStyle(4)
                .viewTextSize(20)
                .build();
        pickerPopWin.showPopWin(CompassActivity.this);
    }

    @OnCheckedChanged(R.id.activity_compass_enable_switch)
    public void enableCompass(CompoundButton buttonView, boolean isChecked){
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
