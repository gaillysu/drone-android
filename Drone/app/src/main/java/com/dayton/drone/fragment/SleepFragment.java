package com.dayton.drone.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dayton.drone.R;

/**
 * Created by boy on 2016/4/24.
 */
public class SleepFragment extends BaseFragment {

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        View view  =View.inflate(mActivity , R.layout.fragment_profrile_content,null);
        return view;
    }
}
