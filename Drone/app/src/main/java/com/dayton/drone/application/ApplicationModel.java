package com.dayton.drone.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by karl-john on 18/3/16.
 */
public class ApplicationModel extends Application {

    public static Context context;

    public static Context getContext() {
        if (context != null) {
            return context;
        } else {
            return ApplicationModel.getContext();
        }
    }

}
