package com.dayton.drone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.model.WorldClock;
import com.dayton.drone.view.SlideView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/5/10.
 */
public class WorldClockAdapter extends BaseAdapter implements SlideView.OnSlideListener {
    private List<WorldClock> list;
    private Context context;

    private DeleteItemInterface deleteItemInterface;
    private SlideView mLastSlideViewWithStatusOn;

    public WorldClockAdapter(List<WorldClock> listData, Context context) {
        this.list = listData;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size() !=0?list.size():0 ;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i)!=null?list.get(i):null ;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        SlideView slideView = (SlideView) convertView;
        if (slideView == null) {
            View itemView = View.inflate(context, R.layout.worlde_clock_adapter_layout, null);

            slideView = new SlideView(context);
            slideView.setContentView(itemView);

            holder = new ViewHolder(slideView);

            slideView.setOnSlideListener(WorldClockAdapter.this);
            slideView.setTag(holder);

        } else {
            holder = (ViewHolder) slideView.getTag();
        }


        WorldClock bean = list.get(position);
        bean.slideView = slideView;
        bean.slideView.shrink();

        if (bean != null) {
            String name = bean.getTimeZoneName();

            TimeZone timeZone = TimeZone.getTimeZone(name);
            Calendar calendar = Calendar.getInstance();
            Calendar LATime = new GregorianCalendar(timeZone);
            LATime.setTimeInMillis(calendar.getTimeInMillis());

            int date = LATime.get(Calendar.DATE);
            int hour = LATime.get(Calendar.HOUR_OF_DAY);
            int minutes = LATime.get(Calendar.MINUTE);

            if(name.contains("_")) {
                name = name.replace("_"," ");
            }
            
                if (name != null && name.contains("/")) {
                    String[] cityDec = name.split("/");
                    holder.cityName.setText(cityDec[1]);
                } else {
                    holder.cityName.setText(name);
                }
            String minutesTime;
            if (minutes < 10) {
                minutesTime = "0" + minutes;
            } else {
                minutesTime = minutes + "";
            }

            String hourDay;
            if (hour < 10) {
                hourDay = "0" + hour;

            } else {
                hourDay = "" + hour;
            }

            if (hour <= 12) {
                holder.cityCurrentTime.setText(hourDay + ":" + minutesTime + " AM");
            } else {
                holder.cityCurrentTime.setText(hourDay + ":" + minutesTime + " PM");
            }

            if (hour == 0) {
                hour = 24;
            }

            Date currentDate = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
            int currentTime = Integer.valueOf(format.format(currentDate).split(" ")[1].split(":")[0]);
            int currentDay = Integer.valueOf(format.format(currentDate).split(" ")[0].split("-")[2]);

            if (currentDay > date) {
                holder.cityDay.setText(context.getResources().getString(R.string.world_clock_Yesterday_tv));
                int cityTimeDifference = 24 - hour + currentTime;
                holder.timeDifference.setText(",-" + cityTimeDifference +
                        context.getResources().getString(R.string.world_clock_city_time_difference_behind));

            } else if (currentDay == date) {

                holder.cityDay.setText(context.getResources().getString(R.string.world_clock_today_tv));

                if (hour > currentTime) {
                    int cityTimeDifference = hour - currentTime;
                    holder.timeDifference.setText(", " + cityTimeDifference +" "+
                            context.getResources().getString(R.string.world_clock_city_time_difference_ahead));
                } else if (hour < currentTime) {
                    int cityTimeDifference = currentTime - hour;
                    holder.timeDifference.setText(", " + cityTimeDifference +" "+
                            context.getResources().getString(R.string.world_clock_city_time_difference_behind));
                } else {
                    holder.timeDifference.setText("");
                }
            } else {

                holder.cityDay.setText(context.getResources().getString(R.string.world_clock_Tomorrow_tv));

                holder.timeDifference.setText(", -" + (24 - currentTime) +" "+
                        context.getResources().getString(R.string.world_clock_city_time_difference_ahead));
            }
        }

        holder.deleteHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemInterface.deleteItem(position);
            }
        });

        return slideView;
    }

    @Override
    public void onSlide(View view, int status) {
        if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
            mLastSlideViewWithStatusOn.shrink();
        }
        if (status == SLIDE_STATUS_ON) {
            mLastSlideViewWithStatusOn = (SlideView) view;
        }
    }


    private class ViewHolder {

        private TextView cityName;
        private TextView cityCurrentTime;
        private TextView cityDay;
        private TextView timeDifference;
        ViewGroup deleteHolder;

        ViewHolder(View view) {
            cityName = (TextView) view.findViewById(R.id.world_clock_item_city);
            cityCurrentTime = (TextView) view.findViewById(R.id.world_clock_item_city_time);
            cityDay = (TextView) view.findViewById(R.id.world_clock_item_current_day);
            timeDifference = (TextView) view.findViewById(R.id.world_clock_item_time_difference);
            deleteHolder= (ViewGroup) view.findViewById(R.id.holder);
        }
    }

    public void onDeleteItemListener(DeleteItemInterface deleteItemInterface) {
        this.deleteItemInterface = deleteItemInterface;
    }

    public interface DeleteItemInterface {
        void deleteItem(int position);
    }

}
