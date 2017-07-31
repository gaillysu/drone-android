package com.dayton.drone.ble.controller;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.ble.datasource.GattAttributesDataSourceImpl;

import net.medcorp.library.ble.controller.ConnectionController;
import net.medcorp.library.ble.controller.OtaController;
import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.listener.OnOtaControllerListener;
import net.medcorp.library.ble.util.Constants;
import net.medcorp.library.ble.util.Optional;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Timer;
import java.util.TimerTask;
import com.dayton.drone.R;
import com.dayton.drone.ble.model.request.dfu.SetDFUModeRequest;

/**
 * Created by med on 16/4/12.
 */
public class OtaControllerImpl implements OtaController {

    private final static String TAG = "OtaControllerImpl";

    private ApplicationModel applicationModel;

    private Optional<OnOtaControllerListener> mOnOtaControllerListener = new Optional<OnOtaControllerListener>();
    private ConnectionController connectionController;

    private Constants.DfuFirmwareTypes dfuFirmwareType = Constants.DfuFirmwareTypes.BLUETOOTH ;


    /** check the OTA is doing or stop */
    private Timer mTimeoutTimer = null;
    public static final int MAX_TIME = 45000;
    private double lastprogress = 0.0;

    private Constants.DFUControllerState state = Constants.DFUControllerState.INIT;
    private double progress = 0.0;
    private boolean manualmode = false;
    //end added

    /**
     * this class is OTA timer:MAX_TIME seconds, when OTA is in progress that got broken, it will fire this timer
     * and check whether the progress has got changed, if no change, it means OTA got stopped,for MCU OTA, it give
     * a way that continue OTA from the broken point, or popup message to user how to do(retry or reinstall battery)
     */
    private class myOTATimerTask extends TimerTask
    {
        @Override
        public void run() {
            //add timeout process when use Nordic dfu library,check the state value to judge OTA is doing or not
            if(dfuFirmwareType == Constants.DfuFirmwareTypes.DISTRIBUTION_ZIP
                    && (state == Constants.DFUControllerState.SEND_FIRMWARE_DATA)) {
                return;
            }
            if (lastprogress == progress) //when no change happened, timeout
            {
                Log.e(TAG, "* * * OTA timeout * * *" + "state = " + state + ",connected:" + isConnected() + ",lastprogress = " + lastprogress + ",progress = " + progress);
                ERRORCODE errorcode = ERRORCODE.TIMEOUT;
                //when start Scan DFU service, perhaps get nothing with 20s, here need again scan it?
                if (state == Constants.DFUControllerState.DISCOVERING && dfuFirmwareType == Constants.DfuFirmwareTypes.DISTRIBUTION_ZIP) {
                    Log.e(TAG, "* * * BLE OTA timeout by no found DFU service * * *");
                    errorcode = ERRORCODE.NODFUSERVICE;
                }
                if (mOnOtaControllerListener.notEmpty()) {
                    mOnOtaControllerListener.get().onError(errorcode);
                }
            } else {
                lastprogress = progress;
            }
        }
    }

    public OtaControllerImpl(ApplicationModel context)
    {
        applicationModel = context;
        connectionController = ConnectionController.Singleton.getInstance(context,new GattAttributesDataSourceImpl(context));
        connectionController.connect();
        EventBus.getDefault().register(this);
    }

    //start public function
    /**
     * start OTA
     * @param filename
     * @param firmwareType
     */
    public void performDFUOnFile(String filename , Constants.DfuFirmwareTypes firmwareType)
    {
        if(!isConnected()) {
            String errorMessage = applicationModel.getString(R.string.ota_connect_error_not_do_ota);
            Log.e(TAG,errorMessage);
            state = Constants.DFUControllerState.INIT;
            Toast.makeText(applicationModel,errorMessage,Toast.LENGTH_LONG).show();
            if(mOnOtaControllerListener.notEmpty()) mOnOtaControllerListener.get().onError(ERRORCODE.NOCONNECTION);
            return;
        }
        lastprogress = 0.0;
        progress = 0.0;
        mTimeoutTimer = new Timer();
        mTimeoutTimer.schedule(new myOTATimerTask(),MAX_TIME, MAX_TIME);
        dfuFirmwareType = firmwareType;
        applicationModel.getSyncController().setHoldRequest(true);
        Log.i(TAG,"***********send OTA command*******");
        state = Constants.DFUControllerState.SEND_START_COMMAND;
        connectionController.sendRequest(new SetDFUModeRequest(applicationModel, (byte) 1));
        if(mOnOtaControllerListener.notEmpty()){
            mOnOtaControllerListener.get().onPrepareOTA(firmwareType);
        }
    }

    @Override
    public void cancelDFU() {
        //do nothing
    }

    @Override
    public void setManualMode(boolean manualmode) {
        //do nothing
    }

    @Override
    public void setOtaMode(boolean otaMode,boolean disConnect)
    {
        connectionController.setOTAMode(otaMode, disConnect);
    }

    /**
     * set hight level listener, it should be a activity (OTA controller view:Activity or one fragment)
     */
    @Override
    public void setOnOtaControllerListener(OnOtaControllerListener listener)
    {
        mOnOtaControllerListener.set(listener);
    }

    @Override
    public Boolean isConnected() {
        return connectionController.isConnected();
    }

    @Override
    public Constants.DFUControllerState getState()
    {
        return state;
    }
    @Override
    public void setState(Constants.DFUControllerState state)
    {
        this.state = state;
    }


    @Override
    public void reset(boolean switch2SyncController) {

        if(mTimeoutTimer!=null) {mTimeoutTimer.cancel();mTimeoutTimer=null;}
        //reset it to INIT status !!!IMPORTANT!!!
        state = Constants.DFUControllerState.INIT;
        //BLE OTA and lunar OTA with DFU library, both need forgetSavedAddress(), so here restore it for next time connection
        if(dfuFirmwareType == Constants.DfuFirmwareTypes.BLUETOOTH || dfuFirmwareType == Constants.DfuFirmwareTypes.DISTRIBUTION_ZIP)
        {
            connectionController.restoreSavedAddress();
        }
        if(manualmode)
        {
            manualmode = false;
            connectionController.forgetSavedAddress();
        }
        //disconnect and reconnect for reading new version
        connectionController.setOTAMode(false, true);
        applicationModel.getSyncController().setHoldRequest(false);
    }

    @Override
    public String getFirmwareVersion() {
        return connectionController.getBluetoothVersion();
    }

    @Override
    public String getSoftwareVersion() {
        return connectionController.getSoftwareVersion();
    }

    @Override
    public void forGetDevice()
    {
        //BLE OTA need repair NEVO, so here forget this nevo when OTA done.
        connectionController.unPairDevice(connectionController.getSaveAddress());
    }

    @Subscribe
    public void onEvent(final BLEConnectionStateChangedEvent event){
        if(mOnOtaControllerListener.notEmpty()) {
            mOnOtaControllerListener.get().connectionStateChanged(event.isConnected());
        }

        if (event.isConnected())
        {
           if (state == Constants.DFUControllerState.DISCOVERING)
            {
                state = Constants.DFUControllerState.OTA_SERVICE_FOUND;
                //kill connectionController and med-library BT service and use dfu library service
                Log.i(TAG,"***********DFU service found,disconnect it,prepare for dfu library*******");
                connectionController.restoreSavedAddress();
                connectionController.disconnect();
            }
        }
        else
        {
            //by BLE peer disconnect when normal mode to ota mode
            if (state == Constants.DFUControllerState.SEND_START_COMMAND)
            {
                state = Constants.DFUControllerState.DISCOVERING;
                Log.i(TAG,"***********set OTA mode,forget it firstly,and scan DFU service*******");
                connectionController.setOTAMode(true, true);
                //when switch to DFU mode, the MAC address has changed to another one
                connectionController.forgetSavedAddress();
                connectionController.connect();
            }
            else if (state == Constants.DFUControllerState.OTA_SERVICE_FOUND)
            {
                Log.i(TAG,"***********dfu library is taking over the OTA*******");
                state = Constants.DFUControllerState.SEND_FIRMWARE_DATA;
                if(mOnOtaControllerListener.notEmpty()) {
                    mOnOtaControllerListener.get().onDFUServiceStarted(event.getAddress());
                }
            }
        }

    }
}
