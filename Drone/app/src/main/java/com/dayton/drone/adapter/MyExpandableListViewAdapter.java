package com.dayton.drone.adapter;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.database.bean.AlarmBean;

import java.util.List;

/**
 * Created by med on 17/6/27.
 */

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {
    private List<AlarmBean> alarmBeanList;
    private Context context;
    private int maskWeekdays[] = {1<<0,1<<1,1<<2,1<<3,1<<4,1<<5,1<<6,1<<7};
    private String weekDays[] = {
            context.getString(R.string.sunday),
            context.getString(R.string.monday),
            context.getString(R.string.tuesday),
            context.getString(R.string.wednesday),
            context.getString(R.string.thursday),
            context.getString(R.string.friday),
            context.getString(R.string.saturday),
            context.getString(R.string.snooze)};

    public  MyExpandableListViewAdapter(Context context,List<AlarmBean> alarmBeanList) {
        this.context = context;
        this.alarmBeanList = alarmBeanList;
    }
    @Override
    public int getGroupCount() {
        return alarmBeanList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return alarmBeanList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return alarmBeanList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.world_clock_alarm_expand_list_parent_item, null);
        }

        TextView alarmTime = (TextView) convertView.findViewById(R.id.fragment_alarmm_list_view_item_alarm_time);
        alarmTime.setText(alarmBeanList.get(groupPosition).getHour()+":" + alarmBeanList.get(groupPosition).getMinute());
        SwitchCompat switchCompat = (SwitchCompat)convertView.findViewById(R.id.fragment_alarmm_list_view_item_alarm_switch);
        switchCompat.setChecked(alarmBeanList.get(groupPosition).isEnable());
        TextView alarmLabel = (TextView) convertView.findViewById(R.id.fragment_alarmm_list_view_item_alarm_label);
        alarmLabel.setText(alarmBeanList.get(groupPosition).getLabel());
        TextView weekdays = (TextView) convertView.findViewById(R.id.fragment_alarm_list_view_item_alarm_repeat_weekdays);
        weekdays.setText(ConvertStatus2WeekdayString(alarmBeanList.get(groupPosition).getStatus()));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.world_clock_alarm_expand_list_child_item, null);
        }
        setWeekdayAndSnooze(convertView,alarmBeanList.get(groupPosition).getStatus());
        EditText alarmEdit =  (EditText) convertView.findViewById(R.id.input_alarm_name_edit);
        alarmEdit.setText(alarmBeanList.get(groupPosition).getLabel());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void setWeekdayAndSnooze(View convertView,byte status)
    {
        CheckBox weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_sunday);
        weekdayCheckbox.setChecked(((int)status&maskWeekdays[0])>0);
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_monday);
        weekdayCheckbox.setChecked(((int)status&maskWeekdays[1])>0);
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_tuesday);
        weekdayCheckbox.setChecked(((int)status&maskWeekdays[2])>0);
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_wednesday);
        weekdayCheckbox.setChecked(((int)status&maskWeekdays[3])>0);
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_thursday);
        weekdayCheckbox.setChecked(((int)status&maskWeekdays[4])>0);
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_friday);
        weekdayCheckbox.setChecked(((int)status&maskWeekdays[5])>0);
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_saturday);
        weekdayCheckbox.setChecked(((int)status&maskWeekdays[6])>0);
        CheckBox snoozeCheckBox = (CheckBox) convertView.findViewById(R.id.alarm_snooze_checkbox);
        snoozeCheckBox.setChecked(((int)status&maskWeekdays[7])>0);

    }
    private String ConvertStatus2WeekdayString(byte status)
    {
        int count = 0;
        String repeatString = "";
        for(int i=0;i<7;i++) {
            if(((int)status&maskWeekdays[i])>0){
                repeatString = repeatString + weekDays[i] + ",";
                count = count + 1;
            }
        }
        if(count==7) {
            repeatString  = context.getString(R.string.every_day);
        } else if(count>0) {
            repeatString = repeatString.substring(0,repeatString.length()-1);//remove last ","
        }
        return repeatString;
    }
}
