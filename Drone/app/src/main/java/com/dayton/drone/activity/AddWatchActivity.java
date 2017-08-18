package com.dayton.drone.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.activity.tutorial.SelectDeviceActivity;
import com.dayton.drone.adapter.AddWatchViewPagerAdapter;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.event.BatteryStatusChangedEvent;
import com.dayton.drone.model.Watches;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.SpUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import net.medcorp.library.ble.event.BLEConnectionStateChangedEvent;
import net.medcorp.library.ble.event.BLEFirmwareVersionReceivedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by med on 16/5/17.
 */
public class AddWatchActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.activity_add_watch_viewpager)
    ViewPager addWatchViewPager;

    @Bind(R.id.activity_add_watch_view_pager_current_page_layout)
    LinearLayout viewPagerGroupLayout;

    @Bind(R.id.activity_addwatch_nowatch_layout)
    LinearLayout noWatchLayout;
    @Bind(R.id.add_watch_activity_page)
    LinearLayout addWatchPage;
    @Bind(R.id.my_toolbar)
    Toolbar mToolbar;

    private TextView batteryStateTextView;
    private TextView versionTextView;
    private TextView connectionStateTextView;

    private List<String> firmwareVersion = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwatch);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }
        ButterKnife.bind(this);
        initToolbar();
        List<View> viewList = new ArrayList<>();
        List<Watches> watchesList = getModel().getWatchesDatabaseHelper().getAll(getModel().getUser().getUserID());
        if (!getModel().getSyncController().isConnected()) {
            noWatchLayout.setVisibility(View.VISIBLE);
        } else {
            watchesList.add(new Watches());
            noWatchLayout.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    noWatchLayout.setVisibility(View.GONE);
                }
            }, 1500);
        }

        for (int i = 0; i < watchesList.size(); i++) {
            LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.activity_addwatch_watchinfo_layout, null);
            viewList.add(linearLayout);
            batteryStateTextView = (TextView) linearLayout.findViewById(R.id.activity_addwatch_watchinfo_layout_batterystate_textview);
            versionTextView = (TextView) linearLayout.findViewById(R.id.activity_addwatch_watchinfo_layout_version_textview);
            connectionStateTextView = (TextView) linearLayout.findViewById(R.id.activity_addwatch_watchinfo_layout_connectionstate_textview);
            if (getModel().getSyncController().isConnected()) {
                connectionStateTextView.setText(R.string.add_watch_connected);
                versionTextView.setText(formatFirmwareVersion(getModel().getSyncController().getFirmwareVersion(), getModel().getSyncController().getSoftwareVersion()));
                getModel().getSyncController().getBattery();
            }
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.select_point);
            } else {
                imageView.setBackgroundResource(R.drawable.uncheck_point_shape);
                lp.leftMargin = 15;
            }
            if (watchesList.size() > 1) {
                viewPagerGroupLayout.addView(imageView, lp);
            }

        }
        addWatchViewPager.setAdapter(new AddWatchViewPagerAdapter(viewList));
        addWatchViewPager.addOnPageChangeListener(this);

    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        TextView titleText = (TextView) mToolbar.findViewById(R.id.toolbar_title_tv);
        titleText.setText(getString(R.string.add_watch_watches));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.activity_add_watch_ota_upgrade_layout)
    public void startOTA() {
        startActivity(OTAActivity.class);
    }

    @OnClick(R.id.activity_add_watch_forget_watch_layout)
    public void forgetNotification() {
        getModel().getSyncController().forgetDevice();
        SpUtils.putBoolean(this, CacheConstants.MUST_SYNC_STEPS, true);
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("logOut", false);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("logOut", false);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.activity_addwatch_add_imagebutton)
    public void addWatch() {
        Intent intent = new Intent(this, SelectDeviceActivity.class);
        int type = 5;
        intent.putExtra("type", type);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < viewPagerGroupLayout.getChildCount(); i++) {
            if (i == position) {
                viewPagerGroupLayout.getChildAt(i).setBackgroundResource(R.drawable.select_point);
            } else {
                viewPagerGroupLayout.getChildAt(i).setBackgroundResource(R.drawable.uncheck_point_shape);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private String formatFirmwareVersion(String bleVersion, String mcuVersion) {
        SpUtils.saveBleVersion(AddWatchActivity.this,bleVersion);
        return bleVersion + "/" + mcuVersion;
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(final BLEConnectionStateChangedEvent stateChangedEvent) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (connectionStateTextView == null) {
                    return;
                }
                if (stateChangedEvent.isConnected()) {
                    connectionStateTextView.setText(R.string.add_watch_connected);
                    getModel().getSyncController().getBattery();
                } else {
                    connectionStateTextView.setText(R.string.add_watch_disconnected);
                }
            }
        });
    }

    @Subscribe
    public void onEvent(final BLEFirmwareVersionReceivedEvent bleFirmwareVersionReceivedEvent) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (bleFirmwareVersionReceivedEvent.getFirmwareTypes() == net.medcorp.library.ble.util.Constants.DfuFirmwareTypes.BLUETOOTH) {
                    firmwareVersion.add(0, bleFirmwareVersionReceivedEvent.getVersion());
                }
                if (bleFirmwareVersionReceivedEvent.getFirmwareTypes() == net.medcorp.library.ble.util.Constants.DfuFirmwareTypes.MCU) {
                    firmwareVersion.add(bleFirmwareVersionReceivedEvent.getVersion());
                }
                if (firmwareVersion.size() == 2) {
                    if (versionTextView != null) {
                        versionTextView.setText(formatFirmwareVersion(firmwareVersion.get(0), firmwareVersion.get(1)));
                    }
                    firmwareVersion.clear();
                }
            }
        });
    }

    @Subscribe
    public void onEvent(final BatteryStatusChangedEvent batteryStatusChangedEvent) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (batteryStateTextView == null) {
                    return;
                }
                if (batteryStatusChangedEvent.getState() == Constants.BatteryStatus.InUse.rawValue()) {
                    batteryStateTextView.setText(batteryStatusChangedEvent.getLevel() + "%");
                } else if (batteryStatusChangedEvent.getState() == Constants.BatteryStatus.Charging.rawValue()) {
                    batteryStateTextView.setText(getString(R.string.add_watch_charging) + "," + batteryStatusChangedEvent.getLevel() + "%");
                } else if (batteryStatusChangedEvent.getState() == Constants.BatteryStatus.Damaged.rawValue()) {
                    batteryStateTextView.setText(R.string.add_watch_damaged);
                } else if (batteryStatusChangedEvent.getState() == Constants.BatteryStatus.Calculating.rawValue()) {
                    batteryStateTextView.setText(R.string.add_watch_calculating);
                }
            }
        });
    }

    @OnClick(R.id.activity_add_watch_open_shop)
    public void openShop() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        String path = getResources().getString(R.string.add_watch_activity_shop_address_path);
        Uri shopPath = Uri.parse(path);
        intent.setData(shopPath);
        startActivity(intent);
    }
}
