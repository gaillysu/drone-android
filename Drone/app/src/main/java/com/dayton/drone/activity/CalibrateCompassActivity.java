package com.dayton.drone.activity;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.widget.ImageView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.activity.tutorial.CalibrateWatchMinuteActivity;
import com.dayton.drone.ble.model.request.StartSystemSettingRequest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/20.
 */
public class CalibrateCompassActivity extends BaseActivity  {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate_compass);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getModel().getSyncController().calibrateCompass(StartSystemSettingRequest.CompassSettingOperationID.Start.rawValue());
    }

    @Override
    protected void onStop() {
        super.onStop();
        getModel().getSyncController().calibrateCompass(StartSystemSettingRequest.CompassSettingOperationID.Stop.rawValue());
    }


    @OnClick(R.id.calibrate_finish_button)
    public void finishCompassCalibration() {
        finish();
    }
}
