package com.dayton.drone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.viewmodel.ChooseCityViewModel;

import java.util.List;

/**
 * Created by Administrator on 2016/6/6.
 */
public class MySearchResultAdapter extends BaseAdapter {
    private Context context;
    private List<ChooseCityViewModel> chooseCityViewModelList;
    public MySearchResultAdapter(Context context , List<ChooseCityViewModel> searchResult){
        this.context = context;
        this.chooseCityViewModelList = searchResult;
    }
    @Override
    public int getCount() {
        return chooseCityViewModelList.size()!=0? chooseCityViewModelList.size():0;
    }

    @Override
    public Object getItem(int i) {
        return chooseCityViewModelList.get(i) != null? chooseCityViewModelList.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = View.inflate(context, R.layout.search_list_item,null);
            holder = new ViewHolder();
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.searchCityName = (TextView) view.findViewById(R.id.choose_adapter_item_tv);
        holder.searchCityName.setText(chooseCityViewModelList.get(i).getDisplayName());

        return view;
    }
    private static class ViewHolder{
        TextView searchCityName;
    }
}
