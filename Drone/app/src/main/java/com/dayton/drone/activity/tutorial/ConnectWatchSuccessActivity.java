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
public class ConnectWatchSuccessActivity extends BaseActivity  {

    @Bind(R.id.watch_version)
    TextView watchVersion;
    @Bind(R.id.watch_buletooth_id)
    TextView watchAddress;
    @Bind(R.id.show_watch_iv)
    ImageView icon;

    private List<String> firmwareVersion = new ArrayList<>();
    private int watchIconId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_watch_success);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_gender_select_text_color);
        }
        ButterKnife.bind(this);
        Intent intent = getIntent();
        watchIconId = intent.getIntExtra("watchIconId", -1);
        icon.setImageResource(watchIconId);
        watchVersion.setText(formatFirmwareVersion(getModel().getSyncController().getSoftwareVersion(),getModel().getSyncController().getFirmwareVersion()));
        watchAddress.setText("MAC: "+intent.getStringExtra("address"));
    }
    @OnClick(R.id.calibrate_watch_button)
    public void calibrateWatch(){
        Intent intent  = new Intent(ConnectWatchSuccessActivity.this ,CalibrateWatchHourActivity.class);
        startActivity(intent);
        finish();
    }

    private String formatFirmwareVersion(String bleVersion,String mcuVersion)
    {
        return "version: "+bleVersion+"/"+mcuVersion;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, SelectDeviceActivity.class);
            int type = getIntent().getIntExtra("type",-1);
            intent.putExtra("type",type);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
