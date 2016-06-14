package com.dayton.drone.activity.tutorial;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by boy on 2016/4/13.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_splash);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_gender_select_text_color);
        }
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
