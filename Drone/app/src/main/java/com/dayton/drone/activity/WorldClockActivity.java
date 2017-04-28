package com.dayton.drone.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/24.
 */
public class WorldClockActivity extends BaseActivity {

    @Bind(android.R.id.tabhost)
    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_clock);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
    }

    @OnClick(R.id.world_clock_back_icon_ib)
    public void backClick() {
        finish();
    }
}
