package com.dayton.drone.activity.tutorial;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dayton.drone.R;
import com.dayton.drone.activity.HomeActivity;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.SpUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.event.BLEFirmwareVersionReceivedEvent;
import net.medcorp.library.ble.event.BLESearchEvent;
import net.medcorp.library.permission.PermissionRequestDialogBuilder;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

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


    @OnClick(R.id.calibrate_watch_back_imagebutton)
    public void backCalibrate(){

    }

    @OnClick(R.id.calibrate_watch_next_imagebutton)
    public void nextCalibrate(){

    }

    @OnClick(R.id.calibrate_next_page_button)
    public void nextCalibrateStep(){

    }

}
