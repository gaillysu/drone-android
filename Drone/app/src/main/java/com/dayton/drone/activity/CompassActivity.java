package com.dayton.drone.activity;

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

    @Bind(R.id.activity_compass_auto_on_duration_textview)
    TextView compassAutoOnDurationTextView;

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
        compassEnableSwitch.setChecked(enableCompass);
        compassAutoOnDurationTextView.setText(formatMinutes(compassAutoOnDuration +""));
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
                        SpUtils.putIntMethod(CompassActivity.this,CacheConstants.COMPASS_AUTO_ON_DURATION,durationMinutes);
                        getModel().getSyncController().setCompassAutoOnDuration(durationMinutes);
                    }
                }).viewStyle(4)
                .viewTextSize(20)
                .dateChose(""+ compassAutoOnDuration)
                .build();
        pickerPopWin.showPopWin(CompassActivity.this);
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
               return theMinute + "m";
           }
           else {
               return theMinute/60 + "h " + theMinute%60 + "m";
           }
        }
    }
}
