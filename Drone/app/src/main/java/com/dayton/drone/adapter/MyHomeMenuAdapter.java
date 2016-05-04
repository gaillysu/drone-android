package com.dayton.drone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.bean.MenuBean;
import com.dayton.drone.utils.UIUtils;

import java.util.List;


public class MyHomeMenuAdapter extends BaseAdapter {

    private List<MenuBean> mData;
    private Context mContext;

    public MyHomeMenuAdapter(List<MenuBean> data) {
        this.mData = data;
        mContext = UIUtils.getContext();
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
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.home_menu_item, null);
            convertView.setTag(holder);
            holder.mMenuIv = (ImageView) convertView.findViewById(R.id.home_adapter_menu_icon);
            holder.mMenuTv = (TextView) convertView.findViewById(R.id.home_adapter_menu_tv);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mData.get(position) != null) {
            MenuBean bean = mData.get(position);
            holder.mMenuIv.setBackgroundResource(bean.getIconId());
            holder.mMenuTv.setText(bean.getDec());
        }
        return convertView;
    }

    private static class ViewHolder {
        ImageView mMenuIv;
        TextView mMenuTv;
    }
}
