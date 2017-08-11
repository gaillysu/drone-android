package com.dayton.drone.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.ble.controller.OtaControllerImpl;
import com.dayton.drone.ble.ota.OtaService;
import com.dayton.drone.utils.SpUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import net.medcorp.library.ble.controller.OtaController;
import net.medcorp.library.ble.listener.OnOtaControllerListener;
import net.medcorp.library.ble.model.response.BLEResponseData;
import net.medcorp.library.ble.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

/**
 * Created by med on 17/7/27.
 */

public class OTAActivity extends BaseActivity implements OnOtaControllerListener {
    private static final String TAG = "OtaActivity";
    private Context context;
    private OtaController otaController;
    private Constants.DfuFirmwareTypes enumFirmwareType = Constants.DfuFirmwareTypes.BLUETOOTH;
    private List<String> firmwareURLs;

    @Bind(R.id.my_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.activity_ota_status_textview)
    TextView otaStatus;
    @Bind(R.id.activity_ota_version_textview)
    TextView version;
    @Bind(R.id.activity_ota_progressBar)
    ProgressBar progressBar;

    @Bind(R.id.activity_ota_start_imageButton)
    Button startButton;


    private final DfuProgressListener dfuProgressListener = new DfuProgressListenerAdapter() {
        @Override
        public void onDeviceConnecting(final String deviceAddress) {
            Log.i(TAG, "***********onDeviceConnecting*******" + deviceAddress);
        }

        @Override
        public void onDfuProcessStarting(final String deviceAddress) {
            Log.i(TAG, "***********onDfuProcessStarting*******" + deviceAddress);
        }

        @Override
        public void onEnablingDfuMode(final String deviceAddress) {
            Log.i(TAG, "***********onEnablingDfuMode*******" + deviceAddress);
        }

        @Override
        public void onFirmwareValidating(final String deviceAddress) {
            Log.i(TAG, "***********onFirmwareValidating*******" + deviceAddress);
        }

        @Override
        public void onDeviceDisconnecting(final String deviceAddress) {
            Log.i(TAG, "***********onDeviceDisconnecting*******" + deviceAddress);
        }

        @Override
        public void onDfuCompleted(final String deviceAddress) {
            Log.i(TAG, "***********onDfuCompleted*******" + deviceAddress);
            // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    OTAActivity.this.onSuccessfulFileTranfered();
                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(OtaService.NOTIFICATION_ID);
                }
            }, 200);
        }

        @Override
        public void onDfuAborted(final String deviceAddress) {
            Log.i(TAG, "***********onDfuAborted*******" + deviceAddress);
            // let's wait a bit until we cancel the notification. When canceled immediately it will be recreated by service again.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    OTAActivity.this.onDFUCancelled();
                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(OtaService.NOTIFICATION_ID);
                }
            }, 200);
        }

        @Override
        public void onProgressChanged(final String deviceAddress, final int percent, final float speed, final float avgSpeed, final int currentPart, final int partsTotal) {
            Log.i(TAG, "***********onProgressChanged*******" + deviceAddress + ",percent = " + percent);
            // We have to wait a bit before canceling notification. This is called before DfuService creates the last notification.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    OTAActivity.this.onTransferPercentage(percent);
                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(OtaService.NOTIFICATION_ID);
                }
            }, 200);
        }

        @Override
        public void onError(final String deviceAddress, final int error, final int errorType, final String message) {
            Log.i(TAG, "***********onError*******" + deviceAddress + ",message:" + message);
            // We have to wait a bit before canceling notification. This is called before DfuService creates the last notification.
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    OTAActivity.this.onError(OtaController.ERRORCODE.EXCEPTION);
                    // if this activity is still open and upload process was completed, cancel the notification
                    final NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    manager.cancel(OtaService.NOTIFICATION_ID);
                }
            }, 200);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ota);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }
        ButterKnife.bind(this);
        initToolbar();
        context = this;
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initOta();
    }
    public List<String> getFirmwareURLs()
    {
        //TODO: the final firmware version should be checked and download form network
        // here only use local firmware to test
        String localFirmware = "smwatch2_r4.zip";
        ArrayList<String> buildinZipFirmware = new ArrayList<>();
        buildinZipFirmware.add(localFirmware);
        SpUtils.saveBleNewVersion(this,"0.04");
        return  buildinZipFirmware;
    }

    public int getLocalFirmwareRawResID()
    {
        return  R.raw.smwatch2_r4;
    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        TextView titleText = (TextView) mToolbar.findViewById(R.id.toolbar_title_tv);
        titleText.setText(getString(R.string.ota));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initOta()
    {
        firmwareURLs = getFirmwareURLs();
        otaController = new OtaControllerImpl(getModel());
        otaController.setOnOtaControllerListener(this);
        version.setText(String.format(getString(R.string.ota_version),SpUtils.getBleVersion(this),SpUtils.getBleNewVersion(this)));
    }

    private void showAlertDialog() {
        new MaterialDialog.Builder(this)
                .title(R.string.ota_alert_title)
                .content(R.string.ota_update_available)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        uploadPressed();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        finish();
                    }
                })
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .cancelable(false)
                .show();

    }
    private void uploadPressed() {
        if (!otaController.isConnected()) {
            Log.e(TAG, context.getString(R.string.ota_connect_error_not_do_ota));
            onError(OtaController.ERRORCODE.NOCONNECTION);
            return;
        }
        String firmware = firmwareURLs.get(0);
        enumFirmwareType = Constants.DfuFirmwareTypes.DISTRIBUTION_ZIP;
        progressBar.setProgress(0);
        otaStatus.setText(R.string.ota_status_ready);
        otaController.performDFUOnFile(firmware, enumFirmwareType);
    }

    private void enableButton(boolean enable) {
        //here enable/disable action and navigation buttons
        startButton.setEnabled(enable);
        if(!enable) {
            startButton.setTextColor(getResources().getColor(R.color.disable_gray_color));
            mToolbar.setNavigationIcon(null);
            mToolbar.setNavigationOnClickListener(null);
        }
        else {
            startButton.setTextColor(getResources().getColor(android.R.color.white));
            mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.md_nav_back));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @OnClick(R.id.activity_ota_start_imageButton)
    public void startOta()
    {
        showAlertDialog();
    }
    @Override
    protected void onResume() {
        super.onResume();
        DfuServiceListenerHelper.registerProgressListener(this, dfuProgressListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        DfuServiceListenerHelper.unregisterProgressListener(this, dfuProgressListener);
    }

    @Override
    public void onBackPressed() {
        if (otaController.getState() != Constants.DFUControllerState.INIT) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        otaController.reset(true);
    }

    @Override
    public void onPrepareOTA(Constants.DfuFirmwareTypes which) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                otaStatus.setText(R.string.ota_status_ready);
                enableButton(false);
            }
        });
    }

    @Override
    public void packetReceived(BLEResponseData packet) {
       //do nothing
    }

    @Override
    public void connectionStateChanged(boolean isConnected) {
        if(isConnected) {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (otaController.getState() == Constants.DFUControllerState.INIT) {
                        otaStatus.setText(R.string.ota_status_ready);
                    }
                }
            });
        }
    }

    @Override
    public void onDFUStarted() {
        Log.i(TAG, "onDFUStarted");
    }

    @Override
    public void onDFUCancelled() {
        Log.i(TAG, "onDFUCancelled");
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                otaController.reset(false);
            }
        });
    }

    @Override
    public void onTransferPercentage(final int percent) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(percent);
            }
        });
    }

    @Override
    public void onSuccessfulFileTranfered() {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                otaController.forGetDevice();
                otaStatus.setText(R.string.ota_sucess);
                otaController.reset(false);
                enableButton(true);
            }
        });
    }

    @Override
    public void onError(final OtaController.ERRORCODE errorCode) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String errorMsg = "";
                otaController.reset(false);
                enableButton(true);
                if (errorCode == OtaController.ERRORCODE.TIMEOUT)
                    errorMsg = getString(R.string.ota_error_timeout);
                else if (errorCode == OtaController.ERRORCODE.NOCONNECTION)
                    errorMsg = context.getString(R.string.ota_connect_error_not_do_ota);
                else if (errorCode == OtaController.ERRORCODE.NODFUSERVICE)
                    errorMsg = context.getString(R.string.ota_error_nofounDFUservice);
                else
                    errorMsg = context.getString(R.string.ota_error_other);
                Log.e(TAG, errorMsg);
                otaStatus.setText(errorMsg);
            }
        });
    }

    @Override
    public void onDFUServiceStarted(final String dfuAddress) {
        Log.i(TAG, "onDFUServiceStarted");
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                otaStatus.setText(R.string.ota_status_performing);
            }
        });
        final DfuServiceInitiator starter = new DfuServiceInitiator(dfuAddress)
                .setKeepBond(false)
                .setForceDfu(false)
                .setPacketsReceiptNotificationsEnabled(true)
                .setPacketsReceiptNotificationsValue(DfuServiceInitiator.DEFAULT_PRN_VALUE);
        starter.setZip(getLocalFirmwareRawResID());
        Log.i(TAG, "***********dfu library starts OtaService*******" + "address = " + dfuAddress);
        starter.start(context, OtaService.class);
    }
    @Override
    public void firmwareVersionReceived(Constants.DfuFirmwareTypes whichfirmware, String version) {
        //do nothing
    }
}
