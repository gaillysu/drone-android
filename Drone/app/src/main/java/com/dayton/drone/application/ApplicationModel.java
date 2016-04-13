package com.dayton.drone.application;

import android.app.Application;
import android.content.Context;

import com.dayton.drone.ble.controller.OtaControllerImpl;
import com.dayton.drone.ble.controller.SyncController;
import com.dayton.drone.ble.controller.SyncControllerImpl;

import net.medcorp.library.ble.controller.OtaController;

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

    private SyncController syncController;
    private OtaController otaController;

    @Override
    public void onCreate() {
        super.onCreate();
        syncController = new SyncControllerImpl(this);
        otaController  = new OtaControllerImpl(this);
    }

    public SyncController getSyncController() {
        return syncController;
    }

    public OtaController getOtaController() {
        return otaController;
    }
}
