package com.dayton.drone.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by boy on 2016/4/22.
 */
public class HomeContentFragment extends Fragment {

    private String mArgument;
    private static final String ARGUMENT = "argument";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            mArgument = bundle.getString(ARGUMENT);
        }
    }

    public static HomeContentFragment newInstance(String argument){
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT,argument);
        HomeContentFragment fragment = new HomeContentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
