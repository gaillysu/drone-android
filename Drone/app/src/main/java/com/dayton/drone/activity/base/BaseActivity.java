package com.dayton.drone.activity.base;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.dayton.drone.application.ApplicationModel;

/**
 * Created by karl-john on 18/3/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ApplicationModel application;

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
}
