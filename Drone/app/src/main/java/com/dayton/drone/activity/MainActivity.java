package com.dayton.drone.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.dayton.drone.Constance;
import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by med on 16/4/12.
 */
public class MainActivity extends BaseActivity {


    private ViewPager vp_tutorial;
    private Button mbtSatrt;
    private int currentItem;

    private int[] mIvarray = {R.mipmap.guide_3, R.mipmap.guide_3,
            R.mipmap.guide_3, R.mipmap.guide_3,
            R.mipmap.guide_3, R.mipmap.guide_3,
            R.mipmap.guide_3, R.mipmap.guide_3,
    };

    private List<ImageView> mVpdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        vp_tutorial = (ViewPager) findViewById(R.id.main_tutoyialpage_vp);
        mbtSatrt = (Button) findViewById(R.id.start_my_watch);
        boolean flage = SpUtils.getBoolean(this, Constance.IS_FIRST, true);
        if (flage) {
            vp_tutorial.setVisibility(View.VISIBLE);
            mVpdata = new ArrayList<>(3);
            SpUtils.putBoolean(this, Constance.IS_FIRST, false);
            initData();
            //add Listener
            vp_tutorial.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position == mVpdata.size()-1) {
                        mbtSatrt.setVisibility(View.VISIBLE);
                    }else {
                        mbtSatrt.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            startActivity(HomeActivity.class);
            finish();
        }

        mbtSatrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(HomeActivity.class);
                finish();
            }
        });
    }


    private void initData() {

        for (int image : mIvarray) {
            ImageView icon = new ImageView(this);
            icon.setBackgroundResource(image);
            mVpdata.add(icon);
        }
        vp_tutorial.setAdapter(new MainVpAdapter());
        currentItem = vp_tutorial.getCurrentItem();
    }


    public class MainVpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mVpdata.size();
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
            ImageView iv = mVpdata.get(position);
            container.addView(iv);
            return iv;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!getModel().getSyncController().isConnected()) {
            getModel().getSyncController().startConnect(false);
        }
    }
}
