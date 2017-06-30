package com.dayton.drone.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TimePicker;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dayton.drone.R;
import com.dayton.drone.adapter.MyExpandableListViewAdapter;
import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.ble.model.DailyAlarmModel;
import com.dayton.drone.database.bean.AlarmBean;
import com.dayton.drone.fragment.listener.OnEditAlarmListener;

import net.medcorp.library.ble.util.Optional;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class WorldClockAlarmFragment extends Fragment implements OnEditAlarmListener {
    @Bind(R.id.world_clock_alarm_expandableListView)
    ExpandableListView expandableListView;

    private MyExpandableListViewAdapter myExpandableListViewAdapter;

    private ApplicationModel getModel() {
        return (ApplicationModel) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.world_clock_alarm_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        refreshAlarmListView();
        return view;
    }

    private void refreshAlarmListView() {
        expandableListView.setGroupIndicator(null);
        expandableListView.setChildIndicator(null);
        List<AlarmBean> alarmBeanList = getModel().getAlarmDatabaseHelper().get();
        myExpandableListViewAdapter = new MyExpandableListViewAdapter(getContext(),alarmBeanList,this);
        expandableListView.setAdapter(myExpandableListViewAdapter);
    }

    /**
     * here these situations to need sync alarm with watch
     * 1:remove or disable
     * 2:enable
     * 3:alarm time got changed (edit or add alarm)
     * 4:alarm status got changed (repeat weekday or snooze enable/disable)
     */
    private void syncAlarm2Watch() {
        List<AlarmBean> alarmBeanList = getModel().getAlarmDatabaseHelper().get();
        List<DailyAlarmModel> dailyAlarmModels = new ArrayList<>();
        for(AlarmBean alarmBean:alarmBeanList)
        {
            if(alarmBean.isEnable()) {
                dailyAlarmModels.add(new DailyAlarmModel(alarmBean.getHour(),alarmBean.getMinute(),alarmBean.getStatus()));
            }
        }
        getModel().getSyncController().setDailyAlarm(dailyAlarmModels);
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
            new TimePickerDialog(getContext(), R.style.TimerPickerStyle, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    AlarmBean alarmBean = new AlarmBean();
                    alarmBean.setHour((byte) hourOfDay);
                    alarmBean.setMinute((byte)minute);
                    alarmBean.setLabel("Alarm");
                    alarmBean.setEnable(true);
                    alarmBean.setStatus((byte)0xFF);
                    Optional<AlarmBean> result = getModel().getAlarmDatabaseHelper().add(alarmBean);
                    if(result.notEmpty()) {
                        refreshAlarmListView();
                    }
                }
            }, dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), true).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAlarmTime(final AlarmBean alarmBean, int hour, int minute) {
        new TimePickerDialog(getContext(), R.style.TimerPickerStyle, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                getModel().getAlarmDatabaseHelper().update(alarmBean,hourOfDay,minute);
                refreshAlarmListView();
                syncAlarm2Watch();
            }
        }, hour, minute, true).show();
    }

    @Override
    public void onAlarmLabel(final AlarmBean alarmBean) {
        new MaterialDialog.Builder(getActivity())
                .content(getString(R.string.alarm_label))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.alarm_label), alarmBean.getLabel(), new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        if (input.length() == 0)
                            return;
                        getModel().getAlarmDatabaseHelper().update(alarmBean,input.toString());
                    }
                }).negativeText(R.string.profile_dialog_negative_button_text)
                .show();
    }

    @Override
    public void onAlarmEnable(AlarmBean alarmBean, boolean enable) {
        getModel().getAlarmDatabaseHelper().update(alarmBean,enable);
        syncAlarm2Watch();
    }

    @Override
    public void onAlarmStatus(AlarmBean alarmBean, byte status) {
        getModel().getAlarmDatabaseHelper().update(alarmBean,status);
        syncAlarm2Watch();
    }

    @Override
    public void onAlarmRemove(AlarmBean alarmBean) {
        getModel().getAlarmDatabaseHelper().remove(alarmBean);
        refreshAlarmListView();
        syncAlarm2Watch();
    }

    @Override
    public void onEditMode2ViewMode() {
        refreshAlarmListView();
    }

}
