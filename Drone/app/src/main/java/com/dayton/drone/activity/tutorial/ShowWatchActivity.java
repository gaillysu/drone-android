package com.dayton.drone.activity.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.HomeActivity;
import com.dayton.drone.activity.base.BaseActivity;

import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.event.BLESearchEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_watch);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        Intent intent = getIntent();
        int watchIconId = intent.getIntExtra("watchIconId", -1);
        icon.setImageResource(watchIconId);
//        if(!getModel().getSyncController().isConnected()) {
//            getModel().getSyncController().startConnect(true);
//        }
//        else{
//            startActivity(HomeActivity.class);
//            finish();
//        }
        startActivity(HomeActivity.class);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                    selectConnectingStatus.setText("Fail");
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
    public void onEvent(BLEConnectionStateChangedEvent event){
        if (event.isConnected()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    selectConnectingStatus.setText("Connected");
                    watchName.setVisibility(View.VISIBLE);
                    watchVersion.setVisibility(View.VISIBLE);
                    buletoothID.setVisibility(View.VISIBLE);
                    //TODO how to go homeActivity? here assume auto go it after some seconds
                    new Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(HomeActivity.class);
                            finish();
                        }
                    },10000);
                }
            });
        }
    }
}