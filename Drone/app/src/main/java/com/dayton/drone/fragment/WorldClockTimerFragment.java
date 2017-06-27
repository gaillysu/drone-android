package com.dayton.drone.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.dayton.drone.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WorldClockTimerFragment extends Fragment {

   @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.world_clock_timer_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    public void initData() {

    }

}
