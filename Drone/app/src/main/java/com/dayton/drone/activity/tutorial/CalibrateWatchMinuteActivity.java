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
public class CalibrateWatchMinuteActivity extends BaseActivity  {
    @Bind(R.id.clockMinute_ImageView)
    ImageView minuteImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate_watch_minute);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.calibrate_watch_reverse_imagebutton)
    public void reverseCalibrate(){
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.MinuteHandReverseOneStep.rawValue());
    }

    @OnClick(R.id.calibrate_watch_advance_imagebutton)
    public void advanceCalibrate(){
        getModel().getSyncController().calibrateWatch(StartSystemSettingRequest.AnalogMovementSettingOperationID.MinuteHandAdvanceOneStep.rawValue());
    }

    @OnClick(R.id.calibrate_next_page_button)
    public void nextCalibrateStep(){
        startActivityWithResultReceiver(new ResultReceiver(null){
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                CalibrateWatchMinuteActivity.this.finish();
                finishActivityWithResultReceiver((ResultReceiver)getIntent().getExtras().getParcelable(getString(R.string.activity_finisher_key)));
            }
        },CalibrateWatchSecondActivity.class);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishActivityWithResultReceiver((ResultReceiver)getIntent().getExtras().getParcelable(getString(R.string.activity_finisher_key)));
    }
}
