package com.dayton.drone.activity.tutorial;

import android.os.Bundle;
import android.os.Handler;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;

/**
 * Created by boy on 2016/4/13.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(WelcomeActivity.class);
                finish();
            }
        },1500);
    }
}
