package com.dayton.drone.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.dayton.drone.application.ApplicationModel;

/**
 * Created by karl-john on 18/3/16.
 */
public class BaseActivity extends AppCompatActivity {

    private ApplicationModel application;

    public ApplicationModel getModel(){
        if (application == null) {
            application = (ApplicationModel) getApplication();
        }
        return application;
    }

    public void startActivity(Class <?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
}
