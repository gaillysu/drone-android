package com.dayton.drone.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.viewmodel.WorldClockViewModel;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class WorldClockAdapter extends RecyclerView.Adapter<WorldClockAdapter.MyViewHolder> {

    private List<WorldClockViewModel> dataList;
    private static final int VIEW_TITLE = 0x01;
    private static final int VIEW_ITEM = 0X02;
    private WorldClockViewModel homeCity;

    public WorldClockAdapter(List<WorldClockViewModel> list) {
        this.dataList = list;
        checkHomeCity(dataList);
    }

    private void checkHomeCity(List<WorldClockViewModel> dataList) {
        for (WorldClockViewModel model : dataList) {
            if (model.isHomeCity()) {
                homeCity = model;
            }
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView cityName, cityDay, cityDifference, cityTime, viewTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.world_clock_item_city);
            cityDay = (TextView) itemView.findViewById(R.id.world_clock_item_current_day);
            cityDifference = (TextView) itemView.findViewById(R.id.world_clock_item_time_difference);
            cityTime = (TextView) itemView.findViewById(R.id.world_clock_item_city_time);
            viewTitle = (TextView) itemView.findViewById(R.id.item_title);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TITLE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.world_clock_adapter_item_title, parent, false);
            return new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.worlde_clock_adapter_layout, parent, false);
            return new MyViewHolder(view);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (homeCity == null) {
            if (position == 0 | position == 1) {
                return VIEW_TITLE;
            }
            return VIEW_ITEM;
        } else {
            if (position == 0 | position == 2) {
                return VIEW_TITLE;
            }
            return VIEW_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (homeCity == null) {
            if (position == 0) {
                holder.viewTitle.setText(R.string.world_clock_adapter_home_time);
            } else if (position == 1) {
                holder.viewTitle.setText(R.string.world_clock_adapter_world_time);
            } else {
                setData( holder, position-2);
            }
        } else {
            if (position == 0) {
                holder.viewTitle.setText(R.string.world_clock_adapter_home_time);
            } else if (position == 1) {
                holder.cityName.setText(homeCity.getName());
                //设置城市具体数据
            } else if (position == 2) {
                holder.viewTitle.setText(R.string.world_clock_adapter_world_time);
            } else {
                setData( holder, position-2);
            }
        }
    }

    private void setData(MyViewHolder holder, int position) {
        WorldClockViewModel been = dataList.get(position);
        holder.cityName.setText(been.getName());
        //设置城市具体数据
    }
}