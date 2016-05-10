package com.dayton.drone.activity.base.tutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.dayton.drone.R;
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
    @Bind(R.id.login_bt)
    Button mLogin;
    @Bind(R.id.registe_bt)
    Button mRegiste;

    private int[] vp_data = new int[]{R.mipmap.drone_mens_black_strap, R.mipmap.drone_mens_tone_split_dial, R.mipmap.drone_white_strap_rosetone,R.mipmap.drone_mens_split_dial};

    private BroadcastReceiver broadcast = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        IntentFilter filter = new IntentFilter();
        filter.addAction("CLOSE_ACTIVITY");
        registerReceiver(broadcast,filter);
        startActivity(LoginActivtiy.class);
    }

    @OnClick(R.id.registe_bt)
    public void openRegisteActivity() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("CLOSE_ACTIVITY");
        registerReceiver(broadcast,filter);
        startActivity(RegisteActivity.class);

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
