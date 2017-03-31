package com.dayton.drone.activity.tutorial;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
public class ShowWatchActivity extends BaseActivity  {

    @Bind(R.id.device_connecting_status)
    TextView selectConnectingStatus;
    @Bind(R.id.retry_connecting)
     Button retryConnecting_bt;
    @Bind(R.id.watch_name)
    TextView watchName;
    @Bind(R.id.watch_version)
    TextView watchVersion;
    @Bind(R.id.watch_buletooth_id)
    TextView buletoothID;
    @Bind(R.id.show_watch_iv)
    ImageView icon;

    private int searchCount = 0;
    private List<String> firmwareVersion = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_watch);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_gender_select_text_color);
        }
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int watchIconId = intent.getIntExtra("watchIconId", -1);
        icon.setImageResource(watchIconId);
        PermissionRequestDialogBuilder builder = new PermissionRequestDialogBuilder(this);
        builder.addPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        builder.askForPermission(this, 1);

        if(!getModel().getSyncController().isConnected()) {
            getModel().getSyncController().startConnect(true);
        }
        else{
            Intent action  = new Intent(this ,HomeActivity.class);
            action.putExtra("logOut",false);
            startActivity(action);
            finish();
        }
    }
    @OnClick(R.id.retry_connecting)
    public void reConnect(){
        searchCount = 0;
        selectConnectingStatus.setText(R.string.bluetooth_connecting);
        retryConnecting_bt.setVisibility(View.INVISIBLE);
        getModel().getSyncController().startConnect(true);
    }
    @Subscribe
    public void onEvent(BLESearchEvent event){
        switch (event.getSearchEvent()) {
            case ON_SEARCH_FAILURE:
                runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    selectConnectingStatus.setText(R.string.show_watch_failed_searching);
                    retryConnecting_bt.setVisibility(View.VISIBLE);
                }
            });
                break;
            case ON_SEARCHING:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchCount++;
                        if((searchCount%4)==0) {
                            selectConnectingStatus.setText(R.string.bluetooth_connecting);
                        }
                        selectConnectingStatus.setText(selectConnectingStatus.getText() + ".");

                    }
                });
                break;
        }
    }

    @Subscribe
    public void onEvent(final BLEConnectionStateChangedEvent event){
        if (event.isConnected()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    selectConnectingStatus.setText(R.string.show_watch_connected);
                    watchName.setVisibility(View.VISIBLE);
                    watchVersion.setVisibility(View.VISIBLE);
                    buletoothID.setVisibility(View.VISIBLE);
                    buletoothID.setText("MAC: "+event.getAddress());
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent  = new Intent(ShowWatchActivity.this ,HomeActivity.class);
                            intent.putExtra("logOut",false);
                            startActivity(intent);
                            finish();
                        }
                    },10000);
                    SpUtils.putBoolean(ShowWatchActivity.this, CacheConstants.MUST_SYNC_STEPS, true);

                }
            });
        }
    }

    @Subscribe
    public void onEvent(final BLEFirmwareVersionReceivedEvent bleFirmwareVersionReceivedEvent) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(bleFirmwareVersionReceivedEvent.getFirmwareTypes() == net.medcorp.library.ble.util.Constants.DfuFirmwareTypes.BLUETOOTH)
                {
                    firmwareVersion.add(0,bleFirmwareVersionReceivedEvent.getVersion());
                }
                if(bleFirmwareVersionReceivedEvent.getFirmwareTypes() == net.medcorp.library.ble.util.Constants.DfuFirmwareTypes.MCU)
                {
                    firmwareVersion.add(bleFirmwareVersionReceivedEvent.getVersion());
                }
                if(firmwareVersion.size()==2)
                {
                    watchVersion.setText(formatFirmwareVersion(firmwareVersion.get(0),firmwareVersion.get(1)));
                    firmwareVersion.clear();
                }
            }
        });
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
