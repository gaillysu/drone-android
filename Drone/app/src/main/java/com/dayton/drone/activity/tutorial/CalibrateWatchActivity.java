package com.dayton.drone.activity.tutorial;

import android.os.Bundle;
import android.widget.ImageView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.ble.model.request.StartSystemSettingRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/20.
 */
public class CalibrateWatchActivity extends BaseActivity  {

    @Bind(R.id.clockHour_ImageView)
    ImageView hourImageView;
    @Bind(R.id.clockMinute_ImageView)
    ImageView minuteImageView;
    @Bind(R.id.clockSecond_ImageView)
    ImageView secondImageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate_watch);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.Start.rawValue());
    }

    @Override
    protected void onStop() {
        super.onStop();
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.Exit.rawValue());
    }

    @OnClick(R.id.calibrate_watch_back_imagebutton)
    public void backCalibrate(){
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.HourHandReverseOneStep.rawValue());
    }

    @OnClick(R.id.calibrate_watch_next_imagebutton)
    public void nextCalibrate(){
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.HourHandAdvanceOneStep.rawValue());
    }

    @OnClick(R.id.calibrate_next_page_button)
    public void nextCalibrateStep(){

    }

}
