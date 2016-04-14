package com.dayton.drone.activity;

import android.os.Bundle;
import android.view.ViewParent;
import android.widget.ImageView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boy on 2016/4/13.
 */
public class LoginActivtiy extends BaseActivity {

    private ViewParent vp_loginPage;
    private List<ImageView> mVpList;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_login);
        initView();
        initDate();
    }

    private void initView() {
        vp_loginPage = (ViewParent) findViewById(R.id.activity_login_vp);
        mVpList = new ArrayList<>(3);
    }

    private void initDate() {
    }
}
