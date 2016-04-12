package com.dayton.drone.ble.controller;

import com.dayton.drone.application.ApplicationModel;

import net.medcorp.library.ble.controller.OtaController;
import net.medcorp.library.ble.listener.OnOtaControllerListener;
import net.medcorp.library.ble.util.Constants;

/**
 * Created by med on 16/4/12.
 */
public class OtaControllerImpl implements OtaController {

    private ApplicationModel applicationModel;
    public OtaControllerImpl(ApplicationModel applicationModel)
    {
        this.applicationModel = applicationModel;
    }

    @Override
    public void performDFUOnFile(String filename, Constants.DfuFirmwareTypes firmwareType) {

    }

    @Override
    public void cancelDFU() {

    }

    @Override
    public void setManualMode(boolean manualmode) {

    }

    @Override
    public void setOnOtaControllerListener(OnOtaControllerListener listener) {

    }

    @Override
    public Boolean isConnected() {
        return null;
    }

    @Override
    public Constants.DFUControllerState getState() {
        return null;
    }

    @Override
    public void setState(Constants.DFUControllerState state) {

    }

    @Override
    public void reset(boolean switch2SyncController) {

    }

    @Override
    public String getFirmwareVersion() {
        return null;
    }

    @Override
    public String getSoftwareVersion() {
        return null;
    }

    @Override
    public void setOtaMode(boolean otaMode, boolean disConnect) {

    }

    @Override
    public void forGetDevice() {

    }
}
