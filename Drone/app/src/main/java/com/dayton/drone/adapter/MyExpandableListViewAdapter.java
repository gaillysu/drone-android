package com.dayton.drone.adapter;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.database.bean.AlarmBean;
import com.dayton.drone.fragment.listener.OnEditAlarmListener;
import com.dayton.drone.view.AnimatedExpandableListView;

import java.util.List;

/**
 * Created by med on 17/6/27.
 */

public class MyExpandableListViewAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private List<AlarmBean> alarmBeanList;
    private Context context;
    private int maskWeekdays[];
    private String weekDays[];
    private OnEditAlarmListener onEditAlarmListener;

    public  MyExpandableListViewAdapter(Context context,List<AlarmBean> alarmBeanList,OnEditAlarmListener onEditAlarmListener) {
        this.context = context;
        this.alarmBeanList = alarmBeanList;
        maskWeekdays = new int[]{1<<0,1<<1,1<<2,1<<3,1<<4,1<<5,1<<6,1<<7};
        weekDays = new String[]{context.getString(R.string.sunday),
                context.getString(R.string.monday),
                context.getString(R.string.tuesday),
                context.getString(R.string.wednesday),
                context.getString(R.string.thursday),
                context.getString(R.string.friday),
                context.getString(R.string.saturday),
                context.getString(R.string.snooze)};

        this.onEditAlarmListener = onEditAlarmListener;
    }
    @Override
    public int getGroupCount() {
        return alarmBeanList.size();
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.world_clock_alarm_expand_list_parent_item, null);
        }

        TextView alarmTime = (TextView) convertView.findViewById(R.id.fragment_alarmm_list_view_item_alarm_time);
        alarmTime.setText(String.format("%02d:%02d",alarmBeanList.get(groupPosition).getHour(),alarmBeanList.get(groupPosition).getMinute()));
        alarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditAlarmListener.onAlarmTime(alarmBeanList.get(groupPosition),alarmBeanList.get(groupPosition).getHour(),alarmBeanList.get(groupPosition).getMinute());
            }
        });

        SwitchCompat switchCompat = (SwitchCompat)convertView.findViewById(R.id.fragment_alarmm_list_view_item_alarm_switch);
        switchCompat.setOnCheckedChangeListener(null);
        switchCompat.setChecked(alarmBeanList.get(groupPosition).isEnable());
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmEnable(alarmBeanList.get(groupPosition),isChecked);
            }
        });
        TextView alarmLabel = (TextView) convertView.findViewById(R.id.fragment_alarmm_list_view_item_alarm_label);
        alarmLabel.setVisibility(isExpanded?View.INVISIBLE:View.VISIBLE);
        alarmLabel.setText(alarmBeanList.get(groupPosition).getLabel());
        TextView weekdays = (TextView) convertView.findViewById(R.id.fragment_alarm_list_view_item_alarm_repeat_weekdays);
        weekdays.setVisibility(isExpanded?View.INVISIBLE:View.VISIBLE);
        weekdays.setText(ConvertStatus2WeekdayString(alarmBeanList.get(groupPosition).getStatus()));
        ImageView imageView = (ImageView)convertView.findViewById(R.id.fragment_alarm_list_view_item_down_image_view);
        imageView.setVisibility(isExpanded?View.INVISIBLE:View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditAlarmListener.onViewMode2EditMode(groupPosition);
            }
        });
        return convertView;
    }

    @Override
    public View getRealChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.world_clock_alarm_expand_list_child_item, null);
        }
        setWeekdayAndSnooze(convertView,alarmBeanList.get(groupPosition));
        TextView textView =  (TextView) convertView.findViewById(R.id.input_alarm_label_text_view);
        textView.setText(alarmBeanList.get(groupPosition).getLabel());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditAlarmListener.onAlarmLabel(alarmBeanList.get(groupPosition));
            }
        });
        ImageView imageView = (ImageView) convertView.findViewById(R.id.fragment_alarm_list_item_delete_image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditAlarmListener.onAlarmRemove(alarmBeanList.get(groupPosition));
            }
        });
        imageView = (ImageView)convertView.findViewById(R.id.fragment_alarm_list_view_up_item_image_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditAlarmListener.onEditMode2ViewMode(groupPosition);
            }
        });

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void setWeekdayAndSnooze(View convertView,final AlarmBean alarmBean)
    {
        CheckBox weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_sunday);
        weekdayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[0])>0);
        weekdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[0]:(int)alarmBean.getStatus()&~maskWeekdays[0]));
            }
        });
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_monday);
        weekdayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[1])>0);
        weekdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[1]:(int)alarmBean.getStatus()&~maskWeekdays[1]));
            }
        });
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_tuesday);
        weekdayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[2])>0);
        weekdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[2]:(int)alarmBean.getStatus()&~maskWeekdays[2]));
            }
        });
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_wednesday);
        weekdayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[3])>0);
        weekdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[3]:(int)alarmBean.getStatus()&~maskWeekdays[3]));
            }
        });
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_thursday);
        weekdayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[4])>0);
        weekdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[4]:(int)alarmBean.getStatus()&~maskWeekdays[4]));
            }
        });
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_friday);
        weekdayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[5])>0);
        weekdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[5]:(int)alarmBean.getStatus()&~maskWeekdays[5]));
            }
        });
        weekdayCheckbox = (CheckBox)  convertView.findViewById(R.id.tag_btn_saturday);
        weekdayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[6])>0);
        weekdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[6]:(int)alarmBean.getStatus()&~maskWeekdays[6]));
            }
        });

        CheckBox snoozeCheckBox = (CheckBox) convertView.findViewById(R.id.alarm_snooze_checkbox);
        snoozeCheckBox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[7])>0);
        snoozeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[7]:(int)alarmBean.getStatus()&~maskWeekdays[7]));
            }
        });

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
        else if(count==0){
            repeatString = context.getString(R.string.no_repeat);
        }
        return repeatString;
    }
}
