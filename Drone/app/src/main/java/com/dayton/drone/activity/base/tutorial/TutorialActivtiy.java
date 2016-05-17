package com.dayton.drone.activity.base.tutorial;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dayton.drone.R;
import com.dayton.drone.activity.HomeActivity;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.TutorialViewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/13.
 */
public class TutorialActivtiy extends BaseActivity implements ViewPager.OnPageChangeListener {

    @Bind(R.id.activity_login_vp)
    ViewPager vp_loginPage;
    @Bind(R.id.view_pager_point_group)
    LinearLayout pointGroup;

    private List<ImageView> mVpList;
    private SwitchPicTask switchPicTask;
    private int[] vp_data = new int[]{R.mipmap.drone_mens_black_strap,
            R.mipmap.drone_mens_tone_split_dial, R.mipmap.drone_white_strap_rosetone,
            R.mipmap.drone_mens_split_dial};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if one user has got logged in, directly entry main menu screen
        if (getModel().getUser().isUserIsLogin()) {
            startActivity(HomeActivity.class);
            finish();
            return;
        }
        setContentView(R.layout.activtiy_login_tutorial);
        ButterKnife.bind(this);
        initDate();

    }

    private void initDate() {
        mVpList = new ArrayList<>(vp_data.length);
        for (int iv : vp_data) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(iv);
            mVpList.add(imageView);
        }
        vp_loginPage.setAdapter(new TutorialViewpagerAdapter(this, mVpList));

        for (int x = 0; x < vp_data.length; x++) {
            ImageView pointImageView = new ImageView(this);
                pointImageView.setBackgroundResource(R.drawable.uncheck_point_shape);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (x == 0) {
                pointImageView.setBackgroundResource(R.drawable.select_point);
            }else{
                lp.leftMargin = 15;
            }
            pointGroup.addView(pointImageView,lp);
        }

        vp_loginPage.addOnPageChangeListener(this);
        int middle = Integer.MAX_VALUE/2;
        int surplus = middle%vp_data.length;
        int selectPoint=middle -surplus;
        vp_loginPage.setCurrentItem(selectPoint);

        if(switchPicTask == null){
            switchPicTask = new SwitchPicTask();
        }
        switchPicTask.start();
        vp_loginPage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
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

    @OnClick(R.id.login_bt)
    public void openLoginActivity() {
        startActivity(LoginActivity.class);
        finish();
    }

    @OnClick(R.id.registe_bt)
    public void openRegisteActivity() {
        startActivity(RegisteActivity.class);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        position = position%vp_data.length;
        int pointChild = pointGroup.getChildCount();
        for(int x = 0; x< pointChild; x++) {
            ImageView point = (ImageView) pointGroup.getChildAt(x);
            point.setBackgroundResource(position == x ? R.drawable.select_point
                    : R.drawable.uncheck_point_shape);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class SwitchPicTask implements Runnable{

        @Override
        public void run() {
           int currentItem = vp_loginPage.getCurrentItem();
            vp_loginPage.setCurrentItem(++currentItem);
            getModel().getHandler().postDelayed(this ,2000);
        }

        public void start(){
            stop();
            getModel().getHandler().postDelayed(this ,2000);
        }

        public void stop(){
            getModel().getHandler().removeCallbacks(this);
        }
    }
}

