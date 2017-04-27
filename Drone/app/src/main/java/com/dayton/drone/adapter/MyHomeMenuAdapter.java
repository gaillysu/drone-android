package com.dayton.drone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.bean.MenuBean;

import java.util.List;


public class MyHomeMenuAdapter extends BaseAdapter {

    private List<MenuBean> mData;
    private Context mContext;
    public MyHomeMenuAdapter(List<MenuBean> data, Context context) {
        this.mData = data;
        mContext = context;
    }


    @Override
    public int getCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (mData != null) {
            return mData.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (holder == null) {

            convertView = View.inflate(mContext, R.layout.home_menu_item, null);
            holder = new ViewHolder();
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mMenuIv = (ImageView) convertView.findViewById(R.id.home_adapter_menu_icon);
        holder.mMenuTv = (TextView) convertView.findViewById(R.id.home_adapter_menu_tv);

        if (mData.get(position) != null) {
            MenuBean bean = mData.get(position);
            holder.mMenuIv.setBackgroundResource(bean.getIconId());
            holder.mMenuTv.setText(bean.getDec());
        }
        return convertView;
    }

    public static class ViewHolder {
        ImageView mMenuIv;
        TextView mMenuTv;
    }
}


