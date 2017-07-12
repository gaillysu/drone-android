package com.dayton.drone.fragment.listener;

import android.support.v7.widget.SwitchCompat;
import android.widget.CheckBox;

import com.dayton.drone.database.bean.AlarmBean;

/**
 * Created by med on 17/6/29.
 */

public interface OnEditAlarmListener {
    public void onAlarmTime(AlarmBean alarmBean,int hour,int minute);
    public void onAlarmLabel(AlarmBean alarmBean);
    public void onAlarmEnable(AlarmBean alarmBean,boolean enable);
    public void onAlarmStatus(AlarmBean alarmBean,byte status);
    public void onAlarmRemove(AlarmBean alarmBean);
    public void onEditMode2ViewMode();
}
