package com.dayton.drone.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.ble.util.Constants;
import com.dayton.drone.utils.SpUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/5/17.
 */

public class HotKeyActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.my_toolbar)
    Toolbar toolbar;
    @Bind(R.id.hot_key_hot_key_switch)
    Switch hotKeySwitch;
    @Bind(R.id.hot_key_select_item)
    LinearLayout selectLayout;
    @Bind(R.id.hot_key_rg)
    RadioGroup allHotKey;
    @Bind(R.id.hot_key_find_phone)
    RadioButton findPhone;
    @Bind(R.id.hot_key_remote_camera)
    RadioButton remoteCamera;
    @Bind(R.id.hot_key_control_music)
    RadioButton controlMusic;
    @Bind(R.id.hor_key_describe)
    TextView describe;

    private TextView toolbarTitle;
    //see@Constants.TopKeyFunction, @BLE profile R4_3
    private static final int REMOTE_CAMERA = 1;
    private static final int FIND_PHONE = 2;
    private static final int CONTROL_MUSIC = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_key_activity);
        ButterKnife.bind(this);
        initToolbar();
        initView();
    }

    private void initView() {
        boolean hotKeyEnable = SpUtils.getHotKeyEnable(this);
        hotKeySwitch.setChecked(hotKeyEnable);
        setHotKeyIsEnable(hotKeyEnable);
        int hotKey = SpUtils.getHotKey(this);
        initHotKey(hotKey);
        hotKeySwitch.setOnCheckedChangeListener(this);
        allHotKey.setOnCheckedChangeListener(this);
    }

    private void initHotKey(int hotKey) {
        switch (hotKey) {
            case FIND_PHONE:
                findPhone.setChecked(true);
                break;
            case REMOTE_CAMERA:
                remoteCamera.setChecked(true);
                break;
            case CONTROL_MUSIC:
                controlMusic.setChecked(true);
                break;
            default:
                findPhone.setChecked(true);
                break;
        }
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

    public void setHotKeyIsEnable(boolean hotKeyIsEnable) {
        if (hotKeyIsEnable) {
            selectLayout.setVisibility(View.VISIBLE);
        } else {
            selectLayout.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        setHotKeyIsEnable(isChecked);
        SpUtils.saveHotKeyEnable(HotKeyActivity.this, isChecked);
        if (isChecked) {
            visibleViewAnimation();
        } else {
            goneViewAnimation();
        }
    }

    private void goneViewAnimation() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(selectLayout, "alpha", 1, 0);
        ObjectAnimator translationX =
                ObjectAnimator.ofFloat(selectLayout, "translationX",0, selectLayout.getWidth());
        AnimatorSet set = new AnimatorSet();
        set.play(translationX).with(alpha);
        set.setDuration(300);
        set.start();
    }

    public void visibleViewAnimation() {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(selectLayout, "alpha", 0, 1);
        ObjectAnimator translationX =
                ObjectAnimator.ofFloat(selectLayout, "translationX", selectLayout.getWidth(), 0);
        AnimatorSet set = new AnimatorSet();
        set.play(translationX).with(alpha);
        set.setDuration(300);
        set.start();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.hot_key_find_phone:
                SpUtils.saveHotKey(this, FIND_PHONE);
                break;
            case R.id.hot_key_remote_camera:
                SpUtils.saveHotKey(this, REMOTE_CAMERA);
                break;
            case R.id.hot_key_control_music:
                SpUtils.saveHotKey(this, CONTROL_MUSIC);
                break;
        }
        getModel().getSyncController().setHotKeyFunction(SpUtils.getHotKey(this));
    }
}