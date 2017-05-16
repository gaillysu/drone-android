package com.dayton.drone.adapter;

import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.viewmodel.DragListViewItem;
import com.dayton.drone.viewmodel.WorldClockCityItemModel;
import com.dayton.drone.viewmodel.WorldClockTitleModel;
import com.woxthebox.draglistview.DragItemAdapter;

import net.medcorp.library.worldclock.City;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Administrator on 2016/5/10.
 */
public class WorldClockAdapter extends DragItemAdapter<Pair<Integer, DragListViewItem>, WorldClockAdapter.ViewHolder> {

    private boolean mDragOnLongPress;
    private ApplicationModel mApplicationModel;
    private Calendar localCalendar;
    private List<Pair<Integer, DragListViewItem>> list;
    private int itemId;
    public static final int VIEW_TYPE_TITLE = 0X01;
    public static final int VIEW_TYPE_ITEM = 0X02;

    public WorldClockAdapter(ApplicationModel applicationModel, int itemId, List<Pair<Integer, DragListViewItem>> list, boolean dragOnLongPress) {
        mDragOnLongPress = dragOnLongPress;
        this.itemId = itemId;
        this.mApplicationModel = applicationModel;
        this.list = list;
        localCalendar = Calendar.getInstance();
        setHasStableIds(true);
        setItemList(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worlde_clock_adapter_layout, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.world_clock_adapter_title, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            setWorldClockData(holder, position);
        } else {
            setTitle(holder, position);
        }
        holder.itemView.setTag(mItemList.get(position));
    }

    private void setTitle(ViewHolder holder, int position) {
        DragListViewItem second = mItemList.get(position).second;
        WorldClockTitleModel title = second.getTitle();
        if (title != null) {
            holder.mTitle.setText(title.getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        DragListViewItem dragListViewItem = list.get(position).second;
        if (dragListViewItem.getTitle() != null) {
            return VIEW_TYPE_TITLE;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }

    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView mTitle, mCityName, mCityDay, mDifference, mCityTime;

        ViewHolder(final View itemView) {
            super(itemView, itemId, mDragOnLongPress);
            mTitle = (TextView) itemView.findViewById(R.id.world_clock_item_title);
            mCityName = (TextView) itemView.findViewById(R.id.world_clock_item_city);
            mCityDay = (TextView) itemView.findViewById(R.id.world_clock_item_current_day);
            mDifference = (TextView) itemView.findViewById(R.id.world_clock_item_time_difference);
            mCityTime = (TextView) itemView.findViewById(R.id.world_clock_item_city_time);
        }
    }

    private String obtainCityTime(City city) {
        String temp = city.getTimezoneRef().getGmt();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"))));
        String am_pm = calendar.get(Calendar.HOUR) < 12 ?
                mApplicationModel.getString(R.string.world_clock_am) : mApplicationModel.getString(R.string.world_clock_pm);
        String minute = calendar.get(Calendar.MINUTE) >= 10 ? calendar.get(Calendar.MINUTE) + "" : "0" + calendar.get(Calendar.MINUTE);
        int hour = calendar.get(Calendar.HOUR);
        return hour + ":" + minute + am_pm;
    }

    private void setWorldClockData(ViewHolder holder, int position) {
        DragListViewItem item = mItemList.get(position).second;
        WorldClockCityItemModel model = item.getItem();
        if (model != null) {
            holder.mCityName.setText(model.getCityName());
            City city = mApplicationModel.getWorldClockDatabaseHelp().get(model.getCityId());
            holder.mCityTime.setText(obtainCityTime(city));
            holder.mCityDay.setText(obtainCityDayDifference(city));
            holder.mDifference.setText(countTimeDifference(city));
        }
    }

    private String countTimeDifference(City city) {
        String temp = city.getTimezoneRef().getGmt();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"))));
        int localDayOfMonth = localCalendar.get(Calendar.DAY_OF_MONTH);
        int homeDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (localDayOfMonth == homeDayOfMonth) {
            return localCalendar.get(Calendar.HOUR) - calendar.get(Calendar.HOUR) > 0 ?
                    localCalendar.get(Calendar.HOUR) - calendar.get(Calendar.HOUR) + mApplicationModel.getString(R.string.world_clock_city_time_difference_ahead) :
                    localCalendar.get(Calendar.HOUR) - calendar.get(Calendar.HOUR) + mApplicationModel.getString(R.string.world_clock_city_time_difference_behind);
        } else if ((localDayOfMonth - homeDayOfMonth) > 0) {

            return localCalendar.get(Calendar.HOUR) + (24 - calendar.get(Calendar.HOUR))
                    + mApplicationModel.getString(R.string.world_clock_city_time_difference_behind);
        } else if (localDayOfMonth - homeDayOfMonth < 0) {
            return localCalendar.get(Calendar.HOUR) + (24 - calendar.get(Calendar.HOUR))
                    + mApplicationModel.getString(R.string.world_clock_city_time_difference_behind);
        } else {
            return "";
        }
    }

    public String obtainCityDayDifference(City city) {
        String temp = city.getTimezoneRef().getGmt();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"))));

        int localDayOfMonth = localCalendar.get(Calendar.DAY_OF_MONTH);
        int homeDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (localDayOfMonth == homeDayOfMonth) {
            return mApplicationModel.getString(R.string.world_clock_today_tv);
        } else {
            int timeDifference = homeDayOfMonth - localDayOfMonth;
            if (timeDifference == 1) {
                return mApplicationModel.getString(R.string.world_clock_Tomorrow_tv);
            } else if (timeDifference == -1) {
                return mApplicationModel.getString(R.string.world_clock_Yesterday_tv);
            } else if (timeDifference > 1) {
                return mApplicationModel.getString(R.string.world_clock_Yesterday_tv);
            } else if (timeDifference < -1) {
                return mApplicationModel.getString(R.string.world_clock_Tomorrow_tv);
            } else {
                return mApplicationModel.getString(R.string.world_clock_today_tv);
            }
        }
    }
}