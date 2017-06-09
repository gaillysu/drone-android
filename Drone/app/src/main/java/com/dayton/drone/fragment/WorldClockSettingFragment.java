package com.dayton.drone.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.dayton.drone.R;
import com.dayton.drone.utils.SpUtils;

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

    @Bind(R.id.world_clock_setting_24h_format_switch)
    SwitchCompat is24HourFormat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.world_clock_setting_fragment, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        int syncTime = SpUtils.getSyncTime(WorldClockSettingFragment.this.getActivity());
        setSyncingText(syncTime);
        isSyncing.setChecked(SpUtils.getIsSyncTime(WorldClockSettingFragment.this.getActivity()));
        setTextColor(SpUtils.getIsSyncTime(WorldClockSettingFragment.this.getActivity()));
        isSyncing.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtils.setIsSyncTime(WorldClockSettingFragment.this.getContext(), isChecked);
                setTextColor(isChecked);
            }
        });

        is24HourFormat.setChecked(SpUtils.get24HourFormat(WorldClockSettingFragment.this.getActivity()));
        is24HourFormat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtils.set24HourFormat(WorldClockSettingFragment.this.getContext(), isChecked);
            }
        });

        syncingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int viewType = 5;
                DatePickerPopWin pickerPopWin = new DatePickerPopWin
                        .Builder(WorldClockSettingFragment.this.getActivity(),
                        new DatePickerPopWin.OnDatePickedListener() {
                            @Override
                            public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                                SpUtils.saveSyncTime(WorldClockSettingFragment.this.getActivity(), month);
                                setSyncingText(month + 1);
                            }
                        }).viewStyle(viewType)
                        .dateChose(getString(R.string.world_clock_adapter_world_time))
                        .viewTextSize(20)
                        .build();
                pickerPopWin.showPopWin(WorldClockSettingFragment.this.getActivity());
            }
        });
    }

    public void setSyncingText(int syncingText) {
        if (syncingText == 1) {
            syncingTime.setText(getString(R.string.world_clock_adapter_world_time));
        } else {
            syncingTime.setText(getString(R.string.world_clock_adapter_home_time));
        }
    }

    public void setTextColor(boolean isSyncing) {
        if (isSyncing) {
            syncingTime.setTextColor(getResources().getColor(R.color.choose_adapter_text_color));
            syncingTimeType.setTextColor(getResources().getColor(R.color.choose_adapter_text_color));
        } else {
            syncingTime.setTextColor(getResources().getColor(R.color.profile_user_bg));
            syncingTimeType.setTextColor(getResources().getColor(R.color.profile_user_bg));
        }
    }
}
