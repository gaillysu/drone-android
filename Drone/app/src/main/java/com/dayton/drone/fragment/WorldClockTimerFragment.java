package com.dayton.drone.fragment;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.dayton.drone.R;
import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.utils.SpUtils;

import java.lang.reflect.Field;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WorldClockTimerFragment extends Fragment {

    @Bind(R.id.time_picker)
    TimePicker timePicker;

    private int countdownInMinutes;

   @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.world_clock_timer_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        countdownInMinutes = SpUtils.getIntMethod(getContext(),getString(R.string.timer_duration),60);
        timePicker.setIs24HourView(true);
        setTimePickerDividerColor();
        timePicker.setCurrentHour(countdownInMinutes/60);
        timePicker.setCurrentMinute(countdownInMinutes%60);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                countdownInMinutes = hourOfDay*60+minute;
                SpUtils.putIntMethod(getContext(),getString(R.string.timer_duration),countdownInMinutes);
            }
        });
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

    public void setTimePickerDividerColor()
    {
        Resources system = Resources.getSystem();
        int hour_numberpicker_id = system.getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id", "android");
        NumberPicker hour_numberpicker = (NumberPicker) timePicker.findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) timePicker.findViewById(minute_numberpicker_id);
        setDividerColor(hour_numberpicker);
        setDividerColor(minute_numberpicker);
    }

    private void setDividerColor(NumberPicker picker) {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields)
        {
            if (pf.getName().equals("mSelectionDivider"))
            {
                pf.setAccessible(true);
                try
                {
                    ColorDrawable colorDrawable = new ColorDrawable();
                    colorDrawable.setColor(Color.WHITE);
                    pf.set(picker, colorDrawable);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
            }
            if (pf.getName().equals("mSelectionDividerHeight"))
            {
                pf.setAccessible(true);
                try {
                    pf.setInt(picker,1);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
