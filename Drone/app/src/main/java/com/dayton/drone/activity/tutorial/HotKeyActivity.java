package com.dayton.drone.activity.tutorial;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/5/17.
 */

public class HotKeyActivity extends BaseActivity {

    @Bind(R.id.my_toolbar)
    Toolbar toolbar;
    @Bind(R.id.hot_key_hot_key_switch)
    Switch hotKeySwitch;
    @Bind(R.id.hot_key_select_item)
    LinearLayout selectLayout;
    @Bind(R.id.hot_key_find_phone_ll)
    LinearLayout findYourPhoneLL;
    @Bind(R.id.hot_key_find_your_phone_iv)
    ImageView findPhoneIv;
    @Bind(R.id.hot_key_find_your_phone_tv)
    TextView findPhoneDescribe;
    @Bind(R.id.hot_key_remote_camera_ll)
    LinearLayout remoteCameraLL;
    @Bind(R.id.hot_key_remote_camera_iv)
    ImageView remoteCameraIv;
    @Bind(R.id.hot_key_remote_camera_tv)
    TextView remoteCameraTv;
    @Bind(R.id.hot_key_control_music_ll)
    LinearLayout controlMusicLL;
    @Bind(R.id.hot_key_control_music_iv)
    ImageView controlMusicIv;
    @Bind(R.id.hot_key_control_music_tv)
    TextView controlMusicTv;

    private TextView toolbarTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_key_activity);
        ButterKnife.bind(this);
        initToolbar();
        initView();
    }

    private void initView() {
        hotKeySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectLayout.setVisibility(View.VISIBLE);
                } else {
                    selectLayout.setVisibility(View.INVISIBLE);
                }
            }
        });

        findYourPhoneLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findPhone();
            }
        });

        remoteCameraLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remoteCamera();
            }
        });

        controlMusicLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlMusic();
            }
        });

    }

    private void initToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title_tv);
        toolbarTitle.setText(getString(R.string.hot_key_toolbar_title));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findPhone() {
        findYourPhoneLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.hot_key_button_bg));
        findPhoneIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_find_your_phone_white));
        findPhoneDescribe.setTextColor(Color.WHITE);

        remoteCameraLL.setBackground(getResources().getDrawable(R.drawable.hot_key_button_def_bg));
        remoteCameraIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera));
        remoteCameraTv.setTextColor(getResources().getColor(R.color.colorPrimary));

        controlMusicLL.setBackground(getResources().getDrawable(R.drawable.hot_key_button_def_bg));
        controlMusicIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_control_music));
        controlMusicTv.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void remoteCamera() {
        findYourPhoneLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.hot_key_button_def_bg));
        findPhoneIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_find_your_phone));
        findPhoneDescribe.setTextColor(getResources().getColor(R.color.colorPrimary));

        remoteCameraLL.setBackground(getResources().getDrawable(R.drawable.hot_key_button_bg));
        remoteCameraIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera_white));
        remoteCameraTv.setTextColor(Color.WHITE);

        controlMusicLL.setBackground(getResources().getDrawable(R.drawable.hot_key_button_def_bg));
        controlMusicIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_control_music));
        controlMusicTv.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    public void controlMusic() {
        findYourPhoneLL.setBackgroundDrawable(getResources().getDrawable(R.drawable.hot_key_button_def_bg));
        findPhoneIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_find_your_phone));
        findPhoneDescribe.setTextColor(getResources().getColor(R.color.colorPrimary));

        remoteCameraLL.setBackground(getResources().getDrawable(R.drawable.hot_key_button_def_bg));
        remoteCameraIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_camera));
        remoteCameraTv.setTextColor(getResources().getColor(R.color.colorPrimary));

        controlMusicLL.setBackground(getResources().getDrawable(R.drawable.hot_key_button_bg));
        controlMusicIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_control_music_white));
        controlMusicTv.setTextColor(Color.WHITE);
    }
}
