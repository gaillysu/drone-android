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

    private int[] watchesArray;
    private String[] watchNameArr;
    private Context context;

    public SelectDeviceGridViewAdapter(int[] watchesArray, String[] watchNameArr, Context context) {
        this.watchesArray = watchesArray;
        this.watchNameArr = watchNameArr;
        this.context = context;

    }

    @Override
    public int getCount() {
        return watchesArray.length;
    }

    @Override
    public Object getItem(int position) {
        return watchesArray[position];
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
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }
        if (watchNameArr.length > 0 && watchesArray.length > 0) {
            holder.watchIcon.setImageResource(watchesArray[position]);
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

