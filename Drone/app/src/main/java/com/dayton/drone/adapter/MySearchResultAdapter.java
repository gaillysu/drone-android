package com.dayton.drone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.model.WorldClock;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class MySearchResultAdapter extends BaseAdapter {
    private Context context;
    private List<WorldClock> searchResult;
    public MySearchResultAdapter(Context context , List<WorldClock> searchResult){
        this.context = context;
        this.searchResult = searchResult;
    }
    @Override
    public int getCount() {
        return searchResult.size()!=0?searchResult.size():0;
    }

    @Override
    public Object getItem(int i) {
        return searchResult.get(i) != null? searchResult.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = View.inflate(context, R.layout.choose_city_adapter_item,null);
            holder = new ViewHolder();
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.searchCityName = (TextView) view.findViewById(R.id.choose_adapter_item_tv);
        holder.searchCityName.setText(searchResult.get(i).getTimeZoneName());

        return null;
    }
    private static class ViewHolder{
        TextView searchCityName;
    }
}
