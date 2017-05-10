package com.dayton.drone.activity.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.application.ApplicationModel;

import net.medcorp.library.ble.event.BLEBluetoothOffEvent;
import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.event.BLESearchEvent;
import net.medcorp.library.permission.PermissionRequestDialogBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by karl-john on 18/3/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ApplicationModel application;
    private Snackbar snackbar;
    protected ApplicationModel getModel(){
        if (application == null) {
            application = (ApplicationModel) getApplication();
        }
        return application;
    }

    protected void startActivity(Class <?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void startActivityWithResultReceiver(ResultReceiver resultReceiver, Class <?> cls) {
        Intent intent = new Intent(this, cls);
        intent.putExtra("finisher", resultReceiver);
        startActivityForResult(intent,1);
    }

    protected void finishActivityWithResultReceiver(ResultReceiver resultReceiver) {
        if(resultReceiver!=null) {
            resultReceiver.send(1, new Bundle());
        }
    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void showStateString(int resId) {
        if(snackbar != null){
            snackbar.dismiss();
        }
        snackbar = Snackbar.make(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), "", Snackbar.LENGTH_LONG);
        TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        tv.setText(getString(resId));
        snackbar.show();
    }
    @Subscribe
    public void onEvent(BLEBluetoothOffEvent event) {
        showStateString(R.string.in_app_notification_bluetooth_disabled);
    }

    @Subscribe
    public void onEvent(BLEConnectionStateChangedEvent event) {
        if (event.isConnected()) {
            showStateString(R.string.in_app_notification_found_watch);
        } else {
            showStateString(R.string.in_app_notification_watch_disconnected);
        }
    }

    @Subscribe
    public void onEvent(final BLESearchEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (event.getSearchEvent() == BLESearchEvent.SEARCH_EVENT.ON_SEARCHING) {
                    PermissionRequestDialogBuilder builder = new PermissionRequestDialogBuilder(BaseActivity.this);
                    builder.addPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
                    builder.askForPermission(BaseActivity.this, 1);
                    showStateString(R.string.in_app_notification_searching);
                }
            }
        });
    }

}
