package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.activity.tutorial.LoginActivity;
import com.dayton.drone.activity.tutorial.WelcomeActivity;
import com.dayton.drone.adapter.MyHomeMenuAdapter;
import com.dayton.drone.bean.MenuBean;
import com.dayton.drone.ble.util.NotificationPermission;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boy on 2016/4/21.
 */
public class HomeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.home_activity_grid_view)
    GridView homeMenu;
    private List<MenuBean> listData;
    private int[] homeMenuPicture;
    private int[] homeMenuIconArray = {R.drawable.ic_activities_icon
            , R.drawable.ic_main_menu_time, R.drawable.ic_main_menu_city_map
            , R.drawable.ic_main_menu_navigation, R.drawable.ic_main_menu_hot_key
            , R.drawable.ic_main_menu_notifications, R.drawable.ic_main_menu_profile
            , R.drawable.ic_main_menu_device};

    private int[] homeMenuIcon = {R.drawable.ic_activities_icon
            , R.drawable.ic_main_menu_time, R.drawable.ic_main_menu_city_map
            , R.drawable.ic_main_menu_navigation, R.drawable.ic_main_menu_hot_key
            , R.drawable.ic_main_menu_notifications, R.drawable.ic_main_menu_login
            , R.drawable.ic_main_menu_device};

    private String[] homeMenuTextArray;

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
        homeMenu.setOnItemClickListener(this);
        NotificationPermission.getNotificationAccessPermission(HomeActivity.this);
    }

    private void initData() {
        listData = new ArrayList<>(homeMenuIconArray.length);
        if (getModel().getUser().isUserIsLogin()) {
            homeMenuTextArray = getResources().getStringArray(R.array.home_menu_text_data);
            homeMenuPicture = homeMenuIconArray;
        } else {
            homeMenuTextArray = getResources().getStringArray(R.array.home_menu);
            homeMenuPicture = homeMenuIcon;
        }
        for (int i = 0; i < homeMenuIconArray.length; i++) {
            MenuBean bean = new MenuBean();
            bean.setIconId(homeMenuPicture[i]);
            bean.setDec(homeMenuTextArray[i]);
            listData.add(bean);
        }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(ActivitiesActivity.class);
                break;
            case 1:
                startActivity(WorldClockActivity.class);
                break;
            case 7:
                if (getModel().getUser().isUserIsLogin()) {
                    Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                    startActivityForResult(intent, 2);
                } else {
                    startActivity(LoginActivity.class);
                    finish();
                }
                break;
            case 8:
                startActivity(AddWatchActivity.class);
                break;
        }

    }
}
