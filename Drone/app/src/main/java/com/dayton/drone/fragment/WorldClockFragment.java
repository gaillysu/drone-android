package com.dayton.drone.fragment;

import android.view.View;

import com.dayton.drone.R;

/**
 * Created by boy on 2016/4/24.
 */
public class WorldClockFragment extends BaseFragment {

    @Override
    protected View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_clock_content,null);
        return view;
    }
}
