package com.dayton.drone.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dayton.drone.R;

/**
 * Created by boy on 2016/4/21.
 */
public class HomeActivity extends FragmentActivity implements View.OnClickListener {

    private ImageView mIvBack,mIvMonthBack,mIvMonthNext,mIvNext;
    private LinearLayout mShowDate;
    private TextView mMonthTv;
    private LinearLayout guide_1,guide_2,guide_3,guide_4,guide_5;
    private FrameLayout mContentFragment;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        addClickListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.back_page);
        mIvMonthBack = (ImageView) findViewById(R.id.back_month);
        mShowDate = (LinearLayout) findViewById(R.id.table_date);
        mIvMonthNext = (ImageView) findViewById(R.id.next_month);
        mIvNext = (ImageView) findViewById(R.id.table_next_page);
        mMonthTv = (TextView) findViewById(R.id.table_date_tv);
        mContentFragment = (FrameLayout) findViewById(R.id.home_middle_content);
        guide_1 = (LinearLayout) findViewById(R.id.home_guide_noe);
        guide_2 = (LinearLayout) findViewById(R.id.home_guide_two);
        guide_3 = (LinearLayout) findViewById(R.id.home_guide_three);
        guide_4  = (LinearLayout) findViewById(R.id.home_guide_four);
        guide_5 = (LinearLayout) findViewById(R.id.home_guide_five);

        mIvBack.setVisibility(View.GONE);
        mIvMonthBack.setVisibility(View.GONE);
        mIvMonthNext.setVisibility(View.GONE);
        mIvNext.setVisibility(View.GONE);
    }

    private void addClickListener() {
        guide_1.setOnClickListener(this);
        guide_2.setOnClickListener(this);
        guide_3.setOnClickListener(this);
        guide_4.setOnClickListener(this);
        guide_5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.home_guide_noe:

                break;
            case R.id.home_guide_two:
                break;
            case R.id.home_guide_three:

                break;
            case R.id.home_guide_four:
                break;
            case R.id.home_guide_five:
                break;
        }

    }
}
