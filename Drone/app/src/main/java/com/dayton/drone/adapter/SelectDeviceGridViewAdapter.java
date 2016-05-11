package com.dayton.drone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boy on 2016/4/27.
 */

/**
 * Created by boy on 2016/4/27.
 */
public class SelectDeviceGridViewAdapter extends BaseAdapter {

    private int[] watchIconArr;
    private String[] watchNameArr;
    private Context context;

    public SelectDeviceGridViewAdapter(int[] watchIconArr, String[] watchNameArr, Context context) {
        this.watchIconArr = watchIconArr;
        this.watchNameArr = watchNameArr;
        this.context = context;

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
            convertView = View.inflate(context, R.layout.item_select_watch, null);
            holder = new HolderView(convertView);
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
        @Bind(R.id.select_watch_icon)
        ImageView watchIcon;
        @Bind(R.id.drone_name)
        TextView watchName;
        public HolderView(View view){
            ButterKnife.bind(this,view);
        }
    }
}

