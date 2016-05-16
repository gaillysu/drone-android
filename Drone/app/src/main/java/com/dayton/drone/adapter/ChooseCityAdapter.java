package com.dayton.drone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.modle.WorldClock;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ChooseCityAdapter extends BaseAdapter{

    private List<WorldClock> chooseCityList;
    private Context context;

    public ChooseCityAdapter(Context context,List<WorldClock> chooseCityList){
        this.chooseCityList = chooseCityList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return chooseCityList.size()!= 0?chooseCityList.size():0;
    }

    @Override
    public Object getItem(int position) {
        return chooseCityList.get(position)!=null?chooseCityList.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder  = null;
        if(convertView == null){
            convertView = View.inflate(context ,R.layout.choose_city_adapter_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        WorldClock  bean = chooseCityList.get(position);
        if(bean != null){
            String cityName = bean.getTimeZoneName();
           String cityDec =  cityName.replace("/",",");
            holder.adapterItemTv.setText(cityDec);
        }
        return convertView;
    }

    static class ViewHolder{
        public ViewHolder(View view){
            ButterKnife.bind(this ,view);
        }
        @Bind(R.id.choose_adapter_item_tv)
        TextView adapterItemTv;

    }
}
