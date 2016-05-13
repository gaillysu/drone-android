package com.dayton.drone.activity.base.tutorial;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dayton.drone.R;
import com.dayton.drone.activity.HomeActivity;
import com.dayton.drone.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/13.
 */
public class TutorialActivtiy extends BaseActivity {

    @Bind(R.id.activity_login_vp)
    ViewPager vp_loginPage;
    private List<ImageView> mVpList;
    private int[] vp_data = new int[]{R.mipmap.drone_mens_black_strap, R.mipmap.drone_mens_tone_split_dial, R.mipmap.drone_white_strap_rosetone,R.mipmap.drone_mens_split_dial};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if one user has got logged in, directly entry main menu screen
        if(getModel().getUser().isUserIsLogin())
        {
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
        vp_loginPage.setAdapter(new MyViewPagerAdapter());
    }

    @OnClick(R.id.login_bt)
    public void openLoginActivity() {
        startActivity(LoginActivtiy.class);
        finish();
    }

    @OnClick(R.id.registe_bt)
    public void openRegisteActivity() {
        startActivity(RegisteActivity.class);
        finish();
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mVpList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = mVpList.get(position);
            container.addView(iv);
            return iv;
        }
    }

}
