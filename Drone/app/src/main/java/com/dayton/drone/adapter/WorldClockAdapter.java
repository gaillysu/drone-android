package com.dayton.drone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.model.WorldClock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/10.
 */
public class WorldClockAdapter extends BaseAdapter {
    private List<WorldClock> list;
    private Context context;
    private boolean isShowEditIcon;
    private String minutesTime;
    private String hourDay;

    private static DeleteItemInterface deleteItemInterface;

    public WorldClockAdapter(List<WorldClock> listData, Context context, boolean flag) {
        this.list = listData;
        this.context = context;
        this.isShowEditIcon = flag;
    }

    @Override
    public int getCount() {
        return list.size() > 0 ? list.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i) == null ? list.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.worlde_clock_adapter_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WorldClock bean = list.get(position);
        if (bean != null) {
            String name = bean.getTimeZoneName();

            TimeZone timeZone = TimeZone.getDefault();
            String zoneId = timeZone.getID();

            Calendar calendar = Calendar.getInstance();
            Calendar LATime = new GregorianCalendar(TimeZone.getTimeZone(name));
            LATime.setTimeInMillis(calendar.getTimeInMillis());
            int date = LATime.get(Calendar.DATE);
            int hour= LATime.get(Calendar.HOUR_OF_DAY);
            int minutes = LATime.get(Calendar.MINUTE);


            if (name != null) {
                String[] cityDec = name.split("/");
                holder.cityName.setText(cityDec[1]);
            }

            if (zoneId.equals(name)) {
                holder.timeDifference.setVisibility(View.GONE);
            }

            if(minutes <10){
                minutesTime = "o"+minutes;
            }else{
                minutesTime = minutes+"";
            }

            if(hour <10){
                hourDay ="0"+hour;

            }else{
                hourDay = ""+hour;
            }

            if (hour > 12) {
                holder.cityCurrentTime.setText(hourDay+ ":" +minutesTime+ " PM");
            } else {
                holder.cityCurrentTime.setText(hourDay + ":" +minutesTime+ " AM");
            }

            Date currentDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int currentTime = new Integer(format.format(currentDate).split(" ")[1].split(":")[0]);
            int currentDay = new Integer(format.format(currentDate).split(" ")[0].split("-")[2]);

            if (currentDay > date) {
                holder.cityDay.setText(R.string.world_clock_Yesterday_tv);

                holder.timeDifference.setText("," + (24 - hour+ currentTime + R.string.world_clock_city_time_difference));
            } else if (currentDay == date) {

                holder.cityDay.setText(R.string.world_clock_today_tv);

                if (hour > currentTime) {
                    holder.timeDifference.setText("," + (hour- currentTime) + R.string.world_clock_city_time_difference);
                } else if(hour<currentTime){
                    holder.timeDifference.setText("," + (currentTime - hour) + R.string.world_clock_city_time_difference);
                }else{
                    holder.timeDifference.setVisibility(View.GONE);
                }
            } else {

                holder.cityDay.setText(R.string.world_clock_Tomorrow_tv);
                holder.timeDifference.setText("," + (24 - currentTime) + R.string.world_clock_city_time_difference);
            }
        }


        if (isShowEditIcon) {
            holder.deleteIcon.setVisibility(View.GONE);
        } else {
            holder.deleteIcon.setVisibility(View.VISIBLE);
        }

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemInterface.deleteItem(position);
            }
        });

        return convertView;
    }


    static class ViewHolder {

        @Bind(R.id.world_clock_item_city)
        TextView cityName;
        @Bind(R.id.world_clock_item_city_time)
        TextView cityCurrentTime;
        @Bind(R.id.world_clock_item_current_day)
        TextView cityDay;
        @Bind(R.id.world_clock_item_time_diffenecer)
        TextView timeDifference;
        @Bind(R.id.world_clock_delete_icon)
        ImageButton deleteIcon;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void onDeleteItemListener(DeleteItemInterface deleteItemInterface) {
        this.deleteItemInterface = deleteItemInterface;
    }

    public interface DeleteItemInterface {
        void deleteItem(int position);
    }

}
