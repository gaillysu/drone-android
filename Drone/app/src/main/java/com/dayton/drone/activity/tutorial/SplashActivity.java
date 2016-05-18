package com.dayton.drone.activity.tutorial;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;

/**
 * Created by boy on 2016/4/13.
 */
public class SplashActivity extends BaseActivity {
    private long time;
    private int currentVersion;
    private String newVersionDescription = null;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_splash);
        //        localVersion();
        //        isNotUpdate();
    }

    private void isNotUpdate() {
        if (checkVersion(currentVersion)) {
            clueUserVersionUpdate();
        } else {
            next();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(WelcomeActivity.class);
                finish();
            }
        },2000);
    }

    public void next() {
        long newTime = System.currentTimeMillis();
        if ((newTime - time) >= 3000) {
            startActivity(WelcomeActivity.class);
            finish();
        } else {
            try {
                Thread.sleep(3000 - (newTime - time));
                startActivity(WelcomeActivity.class);
                finish();
            } catch (InterruptedException e) {
                e.printStackTrace();
                startActivity(WelcomeActivity.class);
                finish();
            }
        }
    }

    /**
     * dialog clue user
     */
    private void clueUserVersionUpdate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.clue_update_dialog);
        builder.setMessage(newVersionDescription);
        builder.setNegativeButton(R.string.cancel_update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(WelcomeActivity.class);
                dialog.dismiss();
                finish();
            }
        });

        builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                downLoadUpdateVersion();
            }
        });
        dialog =  builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }


    /**
     * download new version and install
     */
    private void downLoadUpdateVersion() {
        //TODO
        startActivity(WelcomeActivity.class);
        finish();
    }

    /**
     * app now local version
     */
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

    /**
     * network check the app version
     */
    public boolean checkVersion(int currentVersion) {
        newVersionDescription = "";
        return true;
    }

}
