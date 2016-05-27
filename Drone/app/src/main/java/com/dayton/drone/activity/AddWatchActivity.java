package com.dayton.drone.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.activity.tutorial.SelectDeviceActivity;
import com.dayton.drone.adapter.AddWatchMenuAdapter;
import com.dayton.drone.adapter.AddWatchViewPagerAdapter;
import com.dayton.drone.model.Watches;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by med on 16/5/17.
 */
public class AddWatchActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    @Bind(R.id.activity_addwatch_menu_listview)
    ListView addwatchMenuListview;

    @Bind(R.id.activity_addwatch_viewpager)
    ViewPager addwatchViewPager;

    @Bind(R.id.activity_addwatch_view_pager_currentpage_layout)
    LinearLayout viewPagerGroupLayout;

    @Bind(R.id.activity_addwatch_nowatch_layout)
    LinearLayout noWatchLayout;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwatch);
        ButterKnife.bind(this);
        List<String> listMenu = new ArrayList<String>();
        listMenu.add("Contacts Notifications");
        listMenu.add("Forget this watch");
        addwatchMenuListview.setAdapter(new AddWatchMenuAdapter(listMenu,this));
        addwatchMenuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0) {
                    startActivity(SetNotificationActivity.class);
                }
                else if(position==1) {
                    getModel().getSyncController().forgetDevice();
                }
            }
        });

        List<View> viewList = new ArrayList<>();
        List<Watches>  watchesList = getModel().getWatchesDatabaseHelper().getAll(getModel().getUser().getUserID());
        if(watchesList.isEmpty())
        {
            watchesList.add(new Watches());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    noWatchLayout.setVisibility(View.GONE);
                }
            },1200);
        }
        else
        {
            noWatchLayout.setVisibility(View.GONE);
        }
        for(int i=0;i<watchesList.size();i++) {
            LinearLayout LinearLayout = (LinearLayout) View.inflate(this, R.layout.activity_addwatch_watchinfo_layout, null);
            viewList.add(LinearLayout);
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(i==0) {
                imageView.setBackgroundResource(R.drawable.select_point);
            }
            else {
                imageView.setBackgroundResource(R.drawable.uncheck_point_shape);
                lp.leftMargin = 15;
            }
            if(watchesList.size()>1) {
                viewPagerGroupLayout.addView(imageView, lp);
            }
        }
        addwatchViewPager.setAdapter(new AddWatchViewPagerAdapter(viewList));
        addwatchViewPager.setOnPageChangeListener(this);
    }

    @OnClick(R.id.activity_addwatch_back_imagebutton)
    public void back()
    {
        finish();
    }

    @OnClick(R.id.activity_addwatch_add_imagebutton)
    public void addWatch()
    {
        startActivity(SelectDeviceActivity.class);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int i=0;i<viewPagerGroupLayout.getChildCount();i++) {
            if(i==position) {
                viewPagerGroupLayout.getChildAt(i).setBackgroundResource(R.drawable.select_point);
            }
            else {
                viewPagerGroupLayout.getChildAt(i).setBackgroundResource(R.drawable.uncheck_point_shape);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
