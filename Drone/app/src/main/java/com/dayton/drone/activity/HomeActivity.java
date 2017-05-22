package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.activity.tutorial.WelcomeActivity;
import com.dayton.drone.adapter.MyHomeMenuAdapter;
import com.dayton.drone.bean.menu.ActivitiesMenuItem;
import com.dayton.drone.bean.menu.CityNavigationMenuItem;
import com.dayton.drone.bean.menu.CompassMenuItem;
import com.dayton.drone.bean.menu.DeviceMenuItem;
import com.dayton.drone.bean.menu.HomeMenuItem;
import com.dayton.drone.bean.menu.HotKeyMenuItem;
import com.dayton.drone.bean.menu.LoginMenuItem;
import com.dayton.drone.bean.menu.NotificationMenuItem;
import com.dayton.drone.bean.menu.ProfileMenuItem;
import com.dayton.drone.bean.menu.TimeMenuItem;
import com.dayton.drone.ble.util.NotificationPermission;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boy on 2016/4/21.
 */
public class HomeActivity extends BaseActivity {


    @Bind(R.id.home_activity_grid_view)
    GridView homeMenu;
    private List<HomeMenuItem> listData;
    private HomeMenuItem[] mHomeMenuItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }
        ButterKnife.bind(this);
        initData();
        MyHomeMenuAdapter adapter = new MyHomeMenuAdapter(listData, this);
        homeMenu.setAdapter(adapter);
        NotificationPermission.getNotificationAccessPermission(HomeActivity.this);
    }

    private void initData() {
        listData = new ArrayList<>();
        if (getModel().getUser().isUserIsLogin()) {
            mHomeMenuItemList = new HomeMenuItem[]{new ActivitiesMenuItem(), new TimeMenuItem(),
                    new CityNavigationMenuItem(), new CompassMenuItem(), new HotKeyMenuItem(),
                    new NotificationMenuItem(), new ProfileMenuItem(), new DeviceMenuItem(),};
        } else {
            mHomeMenuItemList = new HomeMenuItem[]{new ActivitiesMenuItem(), new TimeMenuItem(),
                    new CityNavigationMenuItem(), new CompassMenuItem(), new HotKeyMenuItem(),
                    new NotificationMenuItem(), new LoginMenuItem(), new DeviceMenuItem(),};
        }
        for (int i = 0; i < mHomeMenuItemList.length; i++) {
            listData.add(mHomeMenuItemList[i]);
        }
        homeMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(mHomeMenuItemList[position].getActivityClass());
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!getModel().getSyncController().isConnected()) {
            getModel().getSyncController().startConnect(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int myRequestCode = 2 >> 5;
        if (resultCode == myRequestCode) {
            boolean flag = data.getBooleanExtra("logOut", false);
            if (flag) {
                getModel().getUser().setUserIsLogin(false);
                startActivity(WelcomeActivity.class);
                finish();
            } else {
                getModel().getUser().setUserIsLogin(true);
            }
            getModel().getUserDatabaseHelper().update(getModel().getUser());
        }
    }
}
