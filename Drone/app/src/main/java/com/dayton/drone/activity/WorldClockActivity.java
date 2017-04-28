package com.dayton.drone.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.fragment.WorldClockMainFragment;
import com.dayton.drone.fragment.WorldClockSettingFragment;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boy on 2016/4/24.
 */
public class WorldClockActivity extends BaseActivity {

    @Bind(android.R.id.tabhost)
    FragmentTabHost mTabHost;
    private String[] tabName;
    private Class mFragmentArray[] = {WorldClockMainFragment.class, WorldClockSettingFragment.class};
    private int[] mImageArray = {R.drawable.tab_assistant,
            R.drawable.tab_setting_select};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_clock);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }
        ButterKnife.bind(this);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);
        initView();
        mTabHost.setCurrentTab(0);
    }

    private void initView() {
        tabName = getResources().getStringArray(R.array.world_clock_tab_menu);
        for (int i = 0; i < mImageArray.length; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tabName[i]).setIndicator(getImageView(i));
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.user_info_gender_select_text_color);
        }
    }

    private View getImageView(int index) {
        @SuppressLint("InflateParams")
        View view = getLayoutInflater().inflate(R.layout.world_clock_tab_host_item_layout, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.world_clock_tab_host_image);
        TextView tv = (TextView) view.findViewById(R.id.world_clock_tab_host_tv);
        tv.setText(tabName[index]);
        imageView.setImageResource(mImageArray[index]);
        return view;
    }
}
