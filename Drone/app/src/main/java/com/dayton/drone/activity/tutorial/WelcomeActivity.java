package com.dayton.drone.activity.tutorial;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dayton.drone.R;
import com.dayton.drone.activity.ForgetPasswordActivity;
import com.dayton.drone.activity.HomeActivity;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.WelcomeViewpagerAdapter;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import net.medcorp.library.ble.service.BLEServiceProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/13.
 */
public class WelcomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private int[] droneImageViewIdList = new int[]{R.mipmap.welcome_logo_1, R.mipmap.welcome_logo_2,
            R.mipmap.welcome_logo_3, R.mipmap.welcome_logo_4, R.mipmap.welcome_logo_5,R.mipmap.welcome_logo_6};

    @Bind(R.id.activity_login_vp)
    ViewPager viewPager;
    @Bind(R.id.view_pager_point_group)
    LinearLayout pointGroup;

    private SwitchPicTask switchPicTask;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if one user has got logged in, directly entry main menu screen
        if (getModel().getUser().isUserIsLogin()) {
            Intent intent  = new Intent(this ,HomeActivity.class);
            intent.putExtra("logOut",false);
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.activtiy_welcome);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_gender_select_text_color);
        }

        ButterKnife.bind(this);
        handler = new Handler();
        initDate();
        BLEServiceProvider provider = new BLEServiceProvider(this);
        provider.startAdvertising();
    }

    private void initDate() {
        List<ImageView> droneImageViewList = new ArrayList<>(droneImageViewIdList.length);
        for (int iv : droneImageViewIdList) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(iv);
            droneImageViewList.add(imageView);
        }
        viewPager.setAdapter(new WelcomeViewpagerAdapter(droneImageViewList));

        for (int x = 0; x < droneImageViewIdList.length; x++) {
            ImageView pointImageView = new ImageView(this);
            pointImageView.setImageResource(R.drawable.uncheck_point_shape);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (x != 0) {
                lp.leftMargin = dip2px(8f);
            } else {
                pointImageView.setImageResource(R.drawable.select_point);
            }
            pointGroup.addView(pointImageView, lp);
        }

        viewPager.addOnPageChangeListener(this);
        int middle = Integer.MAX_VALUE / 2;
        int surplus = middle % droneImageViewIdList.length;
        int selectPoint = middle - surplus;
        viewPager.setCurrentItem(selectPoint);

        if (switchPicTask == null) {
            switchPicTask = new SwitchPicTask();
        }
        switchPicTask.start();
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        switchPicTask.stop();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        switchPicTask.start();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    @OnClick(R.id.login_bt)
    public void openLoginActivity() {
        startActivity(LoginActivity.class);
        finish();
    }

    @OnClick(R.id.register_bt)
    public void openRegisteActivity() {
        startActivity(RegisterActivity.class);
        finish();
    }

    @OnClick(R.id.open_forget_password_page_bt)
    public void openForgetPasswordPage(){
        startActivity(ForgetPasswordActivity.class);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        position = position%droneImageViewIdList.length;
        int pointChild = pointGroup.getChildCount();
        for (int x = 0; x < pointChild; x++) {
            ImageView point = (ImageView) pointGroup.getChildAt(x);
            point.setImageResource(position == x ? R.drawable.select_point
                    : R.drawable.uncheck_point_shape);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class SwitchPicTask implements Runnable {

        @Override
        public void run() {
            int currentItem = viewPager.getCurrentItem();
            viewPager.setCurrentItem(++currentItem);
            handler.postDelayed(this, 2000);
        }

        public void start() {
            stop();
            handler.postDelayed(this, 2000);
        }

        public void stop() {
            handler.removeCallbacks(this);
        }
    }
}

