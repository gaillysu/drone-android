package com.dayton.drone.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dayton.drone.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/4/28.
 */

public class WorldClockSettingFragment extends Fragment {
    @Bind(R.id.world_clock_setting_syncing_time_switch)
    SwitchCompat isSyncing;
    @Bind(R.id.world_clock_syncing_time_type)
    TextView syncingTimeType;
    @Bind(R.id.world_clock_syncing_time_describe)
    TextView syncingTime;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.world_clock_setting_fragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        isSyncing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    syncingTime.setTextColor(getResources().getColor(R.color.choose_adapter_text_color));
                    syncingTimeType.setTextColor(getResources().getColor(R.color.choose_adapter_text_color));
                }else{
                    syncingTime.setTextColor(getResources().getColor(R.color.profile_user_bg));
                    syncingTimeType.setTextColor(getResources().getColor(R.color.profile_user_bg));
                }
            }
        });

        syncingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WorldClockSettingFragment.this.getContext(),"选择同步时间",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
