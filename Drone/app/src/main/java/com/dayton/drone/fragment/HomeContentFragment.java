package com.dayton.drone.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dayton.drone.R;

/**
 * Created by boy on 2016/4/22.
 */
public class HomeContentFragment extends BaseFragment implements View.OnClickListener {

    private ImageView mIvBack,mIvMonthBack,mIvMonthNext,mIvNext;
    private LinearLayout mShowDate;

    @Override
    protected View initView() {
        View view = View.inflate(mActivity,R.layout.fragment_home_content,null);
        mIvBack = (ImageView) view.findViewById(R.id.back_page);
        mIvMonthBack = (ImageView) view.findViewById(R.id.back_month);
        mShowDate = (LinearLayout) view.findViewById(R.id.table_date);
        mIvMonthNext = (ImageView) view.findViewById(R.id.next_month);
        mIvNext = (ImageView) view.findViewById(R.id.table_next_page);
        mIvBack.setVisibility(View.GONE);
        mIvMonthBack.setVisibility(View.GONE);
        mIvMonthNext.setVisibility(View.GONE);
        mIvNext.setVisibility(View.GONE);
        addListener();
        return view;
    }

    private void addListener() {
        mShowDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    protected void initData() {

    }
}
