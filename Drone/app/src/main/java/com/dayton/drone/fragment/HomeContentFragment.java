package com.dayton.drone.fragment;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.utils.Constance;
import com.dayton.drone.utils.SpUtils;
import com.dayton.drone.utils.UIUtils;
import com.liulishuo.magicprogresswidget.MagicProgressCircle;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/22.
 */
public class HomeContentFragment extends BaseFragment {
    private boolean mIsFirst = true;
    private Context mContext;
    @Bind(R.id.home_fragment_progress_bar)
    MagicProgressCircle mProgressBar;
    @Bind(R.id.home_fragment_progress_middle_tv)
    TextView homeMiddleTv;
    @Bind(R.id.fragment_home_title_calories)
    TextView calories;
    @Bind(R.id.fragment_home_title_miles)
    TextView miles;
    @Bind(R.id.fragment_home_title_active_time)
    TextView activeTime;

    @Bind(R.id.fragment_home_guide_title_active_time)
    LinearLayout guideActiveTime;
    @Bind(R.id.fragment_home_guide_title_miles)
    LinearLayout guideMiles;
    @Bind(R.id.fragment_home_guide_calories)
    LinearLayout guideCalories;
    @Bind(R.id.fragment_home_guide_view)
    RelativeLayout guideView;
    private int guidePage = 1;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home_content, container, false);
        ButterKnife.bind(this, view);
        mContext = UIUtils.getContext();
        mIsFirst = SpUtils.getBoolean(mContext, Constance.IS_FIRST, true);

        mProgressBar.setStartColor(R.color.progress_start_color);
        mProgressBar.setEndColor(R.color.progress_end_color);
        mProgressBar.setSmoothPercent(0.3f);
        homeMiddleTv.setText(200 + "");
        startView();
        return view;
    }

    private void startView() {
        if (mIsFirst) {
            guideView.setVisibility(View.VISIBLE);
            guideActiveTime.setBackgroundResource(R.drawable.registe_edit_bg);
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.fragment_home_guide_view)
    public void guideUser(){
        guidePage = guidePage++;
        switch (guidePage){
            case 1:
                guideCalories.setBackgroundResource(R.drawable.registe_edit_bg);
                guideActiveTime.setBackgroundDrawable(new BitmapDrawable());
                break;
            case 2:
                guideMiles.setBackgroundResource(R.drawable.registe_edit_bg);
                guideCalories.setBackgroundDrawable(new BitmapDrawable());
                break;
            case 3:
                mProgressBar.setBackgroundResource(R.drawable.registe_edit_bg);
                guideMiles.setBackgroundDrawable(new BitmapDrawable());
                break;
            case 4:
                mProgressBar.setBackgroundDrawable(new BitmapDrawable());
                SpUtils.putBoolean(mContext,Constance.IS_FIRST,true);
                guideView.setVisibility(View.GONE);
                break;

        }
    }


}
