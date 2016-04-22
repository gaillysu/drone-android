package com.dayton.drone.activity.base.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.MainActivity;
import com.dayton.drone.activity.base.BaseActivity;

/**
 * Created by boy on 2016/4/20.
 */
public class ShowWatchActivity extends BaseActivity implements View.OnClickListener {
    private TextView selectConnectingStatus;
    private Button retryConnecting_bt;
    private TextView watchName;
    private TextView watchVersion;
    private TextView buletoothID;
    private ImageView icon;
    private String name;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_watch);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        int icon_ic = intent.getIntExtra("image_id", 0);
        name = intent.getStringExtra("name");
        icon = (ImageView) findViewById(R.id.show_watch_iv);
        selectConnectingStatus = (TextView) findViewById(R.id.device_connecting_status);
        retryConnecting_bt = (Button) findViewById(R.id.retry_connecting);
        watchName = (TextView) findViewById(R.id.watch_name);
        watchVersion = (TextView) findViewById(R.id.watch_version);
        buletoothID = (TextView) findViewById(R.id.watch_buletooth_id);
        icon.setBackgroundResource(icon_ic);

        retryConnecting_bt.setVisibility(View.VISIBLE);
        retryConnecting_bt.setOnClickListener(this);
    }

//    private void checkConnDevice() {
//
//        retryConnecting_bt.setVisibility(View.GONE);
//        selectConnectingStatus.setVisibility(View.VISIBLE);
//        watchName.setVisibility(View.GONE);
//        watchVersion.setVisibility(View.GONE);
//        buletoothID.setVisibility(View.GONE);
//        selectConnectingStatus.setText(R.string.bluetooth_connecting);
//        boolean flage = connDevice();
//        if(flage){
//            startActivity(MainActivity.class);
//        }else{
//            selectConnectingStatus.setText(R.string.connecting_fail);
//            retryConnecting.setVisibility(View.VISIBLE);
//
//        }
//    }

//    private boolean connDevice() {
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return false;p
//    }

    @Override
    public void onClick(View v) {
//        if(v.getId() == R.id.retry_connecting){
//            retryConnecting_bt.setVisibility(View.GONE);
//            selectConnectingStatus.setVisibility(View.GONE);
//            watchName.setVisibility(View.VISIBLE);
//            watchVersion.setVisibility(View.VISIBLE);
//            buletoothID.setVisibility(View.VISIBLE);
//
//            boolean flage = deviceConnecting();
//            if(flage){
//                startActivity(MainActivity.class);
//            }else{
//                checkConnDevice();
//            }
//        }
        if(v.getId() == R.id.retry_connecting){
            startActivity(MainActivity.class);
            finish();
        }
    }

    private boolean deviceConnecting() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }
}
