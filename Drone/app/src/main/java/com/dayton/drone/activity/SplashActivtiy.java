package com.dayton.drone.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.activity.base.LoginActivtiy;

/**
 * Created by boy on 2016/4/13.
 */
public class SplashActivtiy extends BaseActivity {
    private long time;
    private int currentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_splash);
        localVersion();
        if(checkVersion(currentVersion)){
            startActivity(LoginActivtiy.class);
        }else{
            long newTime = System.currentTimeMillis();
            if(( newTime - time)>=3000){
                startActivity(LoginActivtiy.class);
            }else{
                try {
                    Thread.sleep(3000 -(newTime-time) );
                    startActivity(LoginActivtiy.class);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    startActivity(LoginActivtiy.class);
                }
            }
        }
    }

    private void localVersion() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            currentVersion = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            //can't reach
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        time = System.currentTimeMillis();
    }

    public boolean checkVersion(int currentVersion){

        return false;
    }

}
