package com.dayton.drone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.bean.WorldClockListBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/10.
 */
public class WorldClockAdapter extends BaseAdapter {
    private List<WorldClockListBean> list;
    private Context context;
    private boolean isShowEditIcon ;
    private static DeleteItemInterface deleteItemInterface;

    public WorldClockAdapter(List<WorldClockListBean> listData ,Context context , boolean flag ) {
        this.list = listData;
        this.context =  context;
        this.isShowEditIcon = flag;
    }

    @Override
    public int getCount() {
        return list.size()>0?list.size():0;
    }

    @Override
    public Object getItem(int i) {
        return list.get(i) == null?list.get(i):null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if(convertView ==null){
            convertView = View.inflate(context, R.layout.worlde_clock_adapter_layout,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        WorldClockListBean bean = list.get(position);
        if(bean != null){
            holder.cityName.setText(bean.getCity());
            holder.cityCurrentTime.setText(bean.getCityCurrentTime());
            holder.cityDay.setText(bean.getModernDate()+".");
            holder.timeDifference.setText(bean.getTimeDifference());
        }



        if(isShowEditIcon){
            holder.deleteIcon.setVisibility(View.GONE);
        }else {
            holder.deleteIcon.setVisibility(View.VISIBLE);
        }

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItemInterface.deleteItem(position);
            }
        });

        return convertView;
    }

    static class ViewHolder{

        @Bind(R.id.world_clock_item_city)
        TextView cityName;
        @Bind(R.id.world_clock_item_city_time)
        TextView cityCurrentTime;
        @Bind(R.id.world_clock_item_current_day)
        TextView cityDay;
        @Bind(R.id.world_clock_item_time_diffecer)
        TextView timeDifference;
        @Bind(R.id.world_clock_delete_icon)
        ImageButton deleteIcon;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }

    public void onDeleteItemListener(DeleteItemInterface deleteItemInterface){
        this.deleteItemInterface = deleteItemInterface;
    }

    public interface DeleteItemInterface{
        void deleteItem(int position);
    }
}