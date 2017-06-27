package com.dayton.drone.fragment;

import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.dayton.drone.R;
import com.dayton.drone.adapter.MyExpandableListViewAdapter;

import org.joda.time.DateTime;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WorldClockAlarmFragment extends Fragment implements TimePickerDialog.OnTimeSetListener {
    @Bind(R.id.world_clock_alarm_expandableListView)
    ExpandableListActivity expandableListView;

    private MyExpandableListViewAdapter myExpandableListViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.world_clock_alarm_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        initData();
        return view;
    }


    public void initData() {

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.toolbar_add_button).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_add_button) {
            DateTime dateTime = new DateTime();
            new TimePickerDialog(getContext(), R.style.TimerPickerStyle, this, dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), true).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //TODO add list and edit it,and save it, and send to watch
    }
}
