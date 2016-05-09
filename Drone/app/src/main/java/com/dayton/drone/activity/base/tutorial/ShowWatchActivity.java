package com.dayton.drone.activity.base.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.HomeActivity;
import com.dayton.drone.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_watch);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        int watchIconId = intent.getIntExtra("watchIconId", -1);
        String selectWatchName = intent.getStringExtra("selectWatchName");
        icon.setBackgroundResource(watchIconId);
        selectConnectingStatus.setVisibility(View.VISIBLE);
        boolean flage =  connectionDervice();

        if(flage){

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("CLOSE_ACTIVITY");
            sendBroadcast(broadcastIntent);
            startActivity(HomeActivity.class);
            finish();
        }
    }

    private boolean connectionDervice(){

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            new Thread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
