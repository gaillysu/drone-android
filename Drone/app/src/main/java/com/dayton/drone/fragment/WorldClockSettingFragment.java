package com.dayton.drone.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dayton.drone.R;

/**
 * Created by Jason on 2017/4/28.
 */

public class WorldClockSettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.world_clock_setting_fragment, container, false);
        return view;
    }
}
