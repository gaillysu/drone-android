package com.dayton.drone.activity.tutorial;

import android.os.Bundle;
import android.os.ResultReceiver;
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
public class CalibrateWatchHourActivity extends BaseActivity  {

    @Bind(R.id.clockHour_ImageView)
    ImageView hourImageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate_watch_hour);
        ButterKnife.bind(this);
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.Start.rawValue());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.Exit.rawValue());
    }

    @OnClick(R.id.calibrate_watch_reverse_imagebutton)
    public void reverseCalibrate(){
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.HourHandReverseOneStep.rawValue());
    }

    @OnClick(R.id.calibrate_watch_advance_imagebutton)
    public void advanceCalibrate(){
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.HourHandAdvanceOneStep.rawValue());
    }

    @OnClick(R.id.calibrate_next_page_button)
    public void nextCalibrateStep(){
        startActivityWithResultReceiver(new ResultReceiver(null){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                CalibrateWatchHourActivity.this.finish();
            }
        },CalibrateWatchMinuteActivity.class);
    }

}
