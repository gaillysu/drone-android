package com.dayton.drone.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.utils.UIUtils;

/**
 * Created by boy on 2016/4/27.
 */

/**
 * Created by boy on 2016/4/27.
 */
public class SelectDeviceGridViewAdapter extends BaseAdapter {

    private int[] watchIconArr;
    private String[] watchNameArr;

    public SelectDeviceGridViewAdapter(int[] watchIconArr, String[] watchNameArr) {
        this.watchIconArr = watchIconArr;
        this.watchNameArr = watchNameArr;
    }

    @Override
    public int getCount() {
        return watchIconArr.length;
    }

    @Override
    public Object getItem(int position) {
        return watchIconArr[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holder = null;
        if (convertView == null) {
            convertView = View.inflate(UIUtils.getContext(), R.layout.item_select_watch, null);
            holder = new HolderView();
            holder.watchIcon = (ImageView) convertView.findViewById(R.id.select_watch_icon);
            holder.watchName = (TextView) convertView.findViewById(R.id.drone_name);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }
        if (watchNameArr.length > 0 && watchIconArr.length > 0) {
            holder.watchIcon.setImageResource(watchIconArr[position]);
            holder.watchName.setText(watchNameArr[position]);
        }
        return convertView;
    }

    static class HolderView {
        ImageView watchIcon;
        TextView watchName;
    }
}

