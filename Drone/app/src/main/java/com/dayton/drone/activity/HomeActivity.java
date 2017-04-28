package com.dayton.drone.activity;

import android.content.Intent;
import android.content.res.TypedArray;
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
import com.dayton.drone.bean.HomeMenuItem;
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
public class HomeActivity extends BaseActivity {

    @Bind(R.id.home_activity_grid_view)
    GridView homeMenu;
    private List<MenuBean> listData;
    private TypedArray homeMenuIconArray;
    private String[] homeMenuTextArray;
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
        homeMenuIconArray = getModel().getUser().isUserIsLogin() ? getResources().obtainTypedArray(R.array.homeMenuIconUser)
                : getResources().obtainTypedArray(R.array.homeMenuIconNoUser);
        listData = new ArrayList<>();
        if (getModel().getUser().isUserIsLogin()) {
            homeMenuTextArray = getResources().getStringArray(R.array.home_menu_text_data);
            mHomeMenuItemList = new HomeMenuItem[]{new HomeMenuItem(0, homeMenuTextArray[0], ActivitiesActivity.class),
                    new HomeMenuItem(1, homeMenuTextArray[1], WorldClockActivity.class),
                    new HomeMenuItem(2, homeMenuTextArray[2], WorldClockActivity.class),
                    new HomeMenuItem(3, homeMenuTextArray[3], WorldClockActivity.class),
                    new HomeMenuItem(4, homeMenuTextArray[4], WorldClockActivity.class),
                    new HomeMenuItem(5, homeMenuTextArray[5], WorldClockActivity.class),
                    new HomeMenuItem(6, homeMenuTextArray[6], ProfileActivity.class),
                    new HomeMenuItem(7, homeMenuTextArray[7], AddWatchActivity.class),
            };
        } else {
            homeMenuTextArray = getResources().getStringArray(R.array.home_menu);
            mHomeMenuItemList = new HomeMenuItem[]{new HomeMenuItem(0, homeMenuTextArray[0], ActivitiesActivity.class),
                    new HomeMenuItem(1, homeMenuTextArray[1], WorldClockActivity.class),
                    new HomeMenuItem(2, homeMenuTextArray[2], WorldClockActivity.class),
                    new HomeMenuItem(3, homeMenuTextArray[3], WorldClockActivity.class),
                    new HomeMenuItem(4, homeMenuTextArray[4], WorldClockActivity.class),
                    new HomeMenuItem(5, homeMenuTextArray[5], WorldClockActivity.class),
                    new HomeMenuItem(6, homeMenuTextArray[6], LoginActivity.class),
                    new HomeMenuItem(7, homeMenuTextArray[7], AddWatchActivity.class),
            };
        }
        for (int i = 0; i < homeMenuTextArray.length; i++) {
            MenuBean bean = new MenuBean();
            bean.setDrawable(homeMenuIconArray.getDrawable(i));
            bean.setDec(homeMenuTextArray[i]);
            listData.add(bean);
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
