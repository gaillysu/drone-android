package com.dayton.drone.adapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.database.bean.AlarmBean;
import com.dayton.drone.fragment.listener.OnEditAlarmListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by med on 17/6/27.
 */

public class MyExpandableListViewAdapter extends RecyclerView.Adapter<MyExpandableListViewAdapter.MyViewHolder> {
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

    public void setAlarmBeanList(List<AlarmBean> alarmBeanList) {
        this.alarmBeanList = alarmBeanList;
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

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.world_clock_alarm_expand_list_parent_item, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.alarmTime.setText(String.format("%02d:%02d",alarmBeanList.get(position).getHour(),alarmBeanList.get(position).getMinute()));
        holder.alarmLabel.setText(alarmBeanList.get(position).getLabel());
        holder.weekdays.setText(ConvertStatus2WeekdayString(alarmBeanList.get(position).getStatus()));
        holder.switchCompat.setChecked(alarmBeanList.get(position).isEnable());
        holder.alarmInputLabel.setText(alarmBeanList.get(position).getLabel());
        holder.switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmEnable(alarmBeanList.get(position),isChecked);
            }
        });
        holder.alarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditAlarmListener.onAlarmTime(alarmBeanList.get(position),alarmBeanList.get(position).getHour(),alarmBeanList.get(position).getMinute());
            }
        });
        final AlarmBean alarmBean = alarmBeanList.get(position);
        holder.sundayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[0])>0);
        holder.sundayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[0]:(int)alarmBean.getStatus()&~maskWeekdays[0]));
            }
        });
        holder.mondayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[1])>0);
        holder.mondayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[1]:(int)alarmBean.getStatus()&~maskWeekdays[1]));
            }
        });
        holder.tuesdayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[2])>0);
        holder.tuesdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[2]:(int)alarmBean.getStatus()&~maskWeekdays[2]));
            }
        });
        holder.wednesdayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[3])>0);
        holder.wednesdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[3]:(int)alarmBean.getStatus()&~maskWeekdays[3]));
            }
        });
        holder.thursdayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[4])>0);
        holder.thursdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[4]:(int)alarmBean.getStatus()&~maskWeekdays[4]));
            }
        });
        holder.fridayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[5])>0);
        holder.fridayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[5]:(int)alarmBean.getStatus()&~maskWeekdays[5]));
            }
        });
        holder.saturdayCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[6])>0);
        holder.saturdayCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[6]:(int)alarmBean.getStatus()&~maskWeekdays[6]));
            }
        });
        holder.snoozeCheckbox.setChecked(((int)alarmBean.getStatus()&maskWeekdays[7])>0);
        holder.snoozeCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onEditAlarmListener.onAlarmStatus(alarmBean,(byte)(isChecked?(int)alarmBean.getStatus()|maskWeekdays[7]:(int)alarmBean.getStatus()&~maskWeekdays[7]));
            }
        });
        holder.alarmInputLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditAlarmListener.onAlarmLabel(alarmBeanList.get(position));
            }
        });
        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onEditAlarmListener.onAlarmRemove(alarmBeanList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return alarmBeanList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        //origin views
        View originView;
        @Bind(R.id.fragment_alarmm_list_view_item_alarm_time)
        TextView alarmTime;
        @Bind(R.id.fragment_alarmm_list_view_item_alarm_repeat_wrapper)
        View alarmRepeatLayout;
        @Bind(R.id.fragment_alarmm_list_view_item_alarm_label)
        TextView alarmLabel;
        @Bind(R.id.fragment_alarm_list_view_item_alarm_repeat_weekdays)
        TextView weekdays;
        @Bind(R.id.fragment_alarmm_list_view_item_alarm_switch)
        SwitchCompat switchCompat;
        @Bind(R.id.fragment_alarm_list_view_item_down_image_view)
        ImageView downImageView;

        //extand views
        @Bind(R.id.edit_alarm_clock_detail_layout)
        View extandView;
        @Bind(R.id.input_alarm_label_text_view)
        TextView alarmInputLabel;
        @Bind(R.id.tag_btn_sunday)
        CheckBox sundayCheckbox;
        @Bind(R.id.tag_btn_monday)
        CheckBox mondayCheckbox;
        @Bind(R.id.tag_btn_tuesday)
        CheckBox tuesdayCheckbox;
        @Bind(R.id.tag_btn_wednesday)
        CheckBox wednesdayCheckbox;
        @Bind(R.id.tag_btn_thursday)
        CheckBox thursdayCheckbox;
        @Bind(R.id.tag_btn_friday)
        CheckBox fridayCheckbox;
        @Bind(R.id.tag_btn_saturday)
        CheckBox saturdayCheckbox;
        @Bind(R.id.alarm_snooze_checkbox)
        CheckBox snoozeCheckbox;
        @Bind(R.id.fragment_alarm_list_item_operation_layout)
        View operationLayout;
        @Bind(R.id.fragment_alarm_list_item_delete_image_view)
        ImageView deleteImageView;
        @Bind(R.id.fragment_alarm_list_view_up_item_image_view)
        ImageView upImageView;

        int originalHeight = 0;
        boolean isViewExpanded = false;

        public MyViewHolder(View view)
        {
            super(view);
            ButterKnife.bind(this, view);
            originView = view;
            alarmRepeatLayout.setOnClickListener(this);
            operationLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            if(view.getId() == R.id.fragment_alarmm_list_view_item_alarm_repeat_wrapper
                    || view.getId() == R.id.fragment_alarm_list_item_operation_layout)
            {
                if (originalHeight == 0) {
                    originalHeight = originView.getHeight();
                }
                ValueAnimator valueAnimator;
                if (!isViewExpanded) {
                    isViewExpanded = true;
                    valueAnimator = ValueAnimator.ofInt(originalHeight, originalHeight + (int) (originalHeight * 1.5));
                } else {
                    isViewExpanded = false;
                    valueAnimator = ValueAnimator.ofInt(originalHeight + (int) (originalHeight * 1.5), originalHeight);
                }
                originView.setBackgroundResource(isViewExpanded?R.color.choose_adapter_list_item_view_color:R.color.notification_bg_color);
                extandView.setBackgroundResource(isViewExpanded?R.color.choose_adapter_list_item_view_color:R.color.notification_bg_color);
                extandView.setVisibility(isViewExpanded?View.VISIBLE:View.GONE);
                downImageView.setVisibility(isViewExpanded?View.INVISIBLE:View.VISIBLE);
                upImageView.setVisibility(isViewExpanded?View.VISIBLE:View.INVISIBLE);
                alarmLabel.setVisibility(isViewExpanded?View.INVISIBLE:View.VISIBLE);
                weekdays.setVisibility(isViewExpanded?View.INVISIBLE:View.VISIBLE);
                if(!isViewExpanded) {
                    onEditAlarmListener.onEditMode2ViewMode();
                }
                valueAnimator.setDuration(100);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        originView.getLayoutParams().height = value.intValue();
                        originView.requestLayout();
                    }
                });
                valueAnimator.start();
            }
        }
    }
}
