package com.dayton.drone.fragment;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;


import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dayton.drone.R;
import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.utils.SpUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorldClockTimerFragment extends Fragment {

    @Bind(R.id.world_clock_timer_duration_textview)
    TextView duration;

    private int countdownInMinutes;

   @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.world_clock_timer_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        countdownInMinutes = SpUtils.getIntMethod(getContext(),getString(R.string.timer_duration),60);
        duration.setText(String.format("%02d:%02d",countdownInMinutes/60,countdownInMinutes%60));
        return view;
    }

    private ApplicationModel getModel() {
        return (ApplicationModel) getActivity().getApplication();
    }

    @OnClick(R.id.world_clock_sync_timer_layout)
    public void setCountdownTimer()
    {
        getModel().getSyncController().setCountdownTimer(countdownInMinutes);
    }

    @OnClick(R.id.world_clock_reset_timer_layout)
    public void resetCountdownTimer()
    {
        countdownInMinutes = 1;
        SpUtils.putIntMethod(getContext(),getString(R.string.timer_duration),countdownInMinutes);
        getModel().getSyncController().setCountdownTimer(countdownInMinutes);
    }

    @OnClick(R.id.world_clock_timer_duration_layout)
    public void editCountdownTimer()
    {
        new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                duration.setText(String.format("%02d:%02d",hourOfDay,minute));
                countdownInMinutes = hourOfDay*60+minute;
                SpUtils.putIntMethod(getContext(),getString(R.string.timer_duration),countdownInMinutes);
            }
        },countdownInMinutes/60,countdownInMinutes%60,true).show();
    }

}
