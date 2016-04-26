package com.dayton.drone.activity.base.tutorial;

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

/**
 * Created by boy on 2016/4/13.
 */
public class TutorialActivtiy extends BaseActivity {

    private ViewPager vp_loginPage;
    private List<ImageView> mVpList;
    private Button mLogin;
    private Button mRegiste;
    private int[] vp_data = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_login_tutorial);
        initView();
        initDate();
        addListener();
    }

    private void initView() {
        vp_loginPage = (ViewPager) findViewById(R.id.activity_login_vp);
        mLogin = (Button) findViewById(R.id.login_bt);
        mRegiste = (Button) findViewById(R.id.registe_bt);
        mVpList = new ArrayList<>(3);
    }

    private void initDate() {
        for (int iv : vp_data) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(iv);
            mVpList.add(imageView);
        }
        vp_loginPage.setAdapter(new MyViewPagerAdapter());
    }

    private void addListener() {
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivtiy.class);
            }
        });

        mRegiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisteActivity.class);
            }
        });
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
