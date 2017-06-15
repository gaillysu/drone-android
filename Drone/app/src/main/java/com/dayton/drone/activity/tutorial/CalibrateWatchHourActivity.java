package com.dayton.drone.activity.tutorial;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.ble.model.request.StartSystemSettingRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Created by boy on 2016/4/20.
 */
public class CalibrateWatchHourActivity extends BaseActivity implements View.OnTouchListener {

    @Bind(R.id.clockHour_ImageView)
    ImageView hourImageView;

    @Bind(R.id.calibrate_watch_reverse_imagebutton)
    ImageButton reverseImageButton;

    @Bind(R.id.calibrate_watch_advance_imagebutton)
    ImageButton advanceImageButton;

    boolean isLongPress = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate_watch_hour);
        ButterKnife.bind(this);
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.Start.rawValue());
        reverseImageButton.setOnTouchListener(this);
        advanceImageButton.setOnTouchListener(this);
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
    @OnLongClick(R.id.calibrate_watch_reverse_imagebutton)
    public boolean reverseCalibrateContinuously(){
        isLongPress = true;
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.HourHandReverseMoreSteps.rawValue());
        return true;
    }

    @OnClick(R.id.calibrate_watch_advance_imagebutton)
    public void advanceCalibrate(){
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.HourHandAdvanceOneStep.rawValue());
    }
    @OnLongClick(R.id.calibrate_watch_advance_imagebutton)
    public boolean advanceCalibrateContinuously(){
        isLongPress = true;
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.HourHandAdvanceMoreSteps.rawValue());
        return true;
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP && isLongPress)
        {
            isLongPress = false;
            getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.StopAllHands.rawValue());
        }
        return false;
    }
}
