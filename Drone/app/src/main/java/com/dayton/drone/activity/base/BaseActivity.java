package com.dayton.drone.activity.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

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
}
