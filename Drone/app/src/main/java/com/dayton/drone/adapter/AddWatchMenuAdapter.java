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

import butterknife.Bind;
import butterknife.ButterKnife;


public class AddWatchMenuAdapter extends BaseAdapter {

    private List<String> menuList;
    private Context context;

    public AddWatchMenuAdapter(List<String> data, Context context) {
        this.menuList = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (menuList != null) {
            return menuList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if (menuList != null) {
            return menuList.get(position);
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
        if(convertView == null)
        {
            convertView = View.inflate(context, R.layout.addwatch_menu_item, null);
        }
        TextView textView = (TextView)convertView.findViewById(R.id.addwatch_menu_item_name_textview);
        textView.setText(menuList.get(position));
        return convertView;
    }

}

