package com.dayton.drone.activity.base.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;

/**
 * Created by boy on 2016/4/20.
 */
public class ShowWatchActivity extends BaseActivity  {
    private TextView selectConnectingStatus;
    private Button retryConnecting_bt;
    private TextView watchName;
    private TextView watchVersion;
    private TextView buletoothID;
    private ImageView icon;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_watch);
        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        int watchIconId = intent.getIntExtra("watchIconId", 0);
        String selectWatchName = intent.getStringExtra("selectWatchName");
        icon = (ImageView) findViewById(R.id.show_watch_iv);
        selectConnectingStatus = (TextView) findViewById(R.id.device_connecting_status);
        retryConnecting_bt = (Button) findViewById(R.id.retry_connecting);
        watchName = (TextView) findViewById(R.id.watch_name);
        watchVersion = (TextView) findViewById(R.id.watch_version);
        buletoothID = (TextView) findViewById(R.id.watch_buletooth_id);
        icon.setBackgroundResource(watchIconId);
        selectConnectingStatus.setVisibility(View.VISIBLE);
    }
}
