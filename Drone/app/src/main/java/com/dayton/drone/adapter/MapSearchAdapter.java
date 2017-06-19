package com.dayton.drone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.network.response.model.AddressComponent;
import com.dayton.drone.network.response.model.GeocodeResult;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by med on 17/5/16.
 */

public class MapSearchAdapter extends BaseAdapter {

    private Context context;
    List<GeocodeResult> geocodeResultList;

    public MapSearchAdapter(Context context,List<GeocodeResult> geocodeResultList) {
        this.context = context;
        this.geocodeResultList = geocodeResultList;
    }

    @Override
    public int getCount() {
        return geocodeResultList.size();
    }

    @Override
    public Object getItem(int position) {
        return geocodeResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.map_search_adapter_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.address.setText(geocodeResultList.get(position).getFormattedCityRegion());
        viewHolder.road.setText(geocodeResultList.get(position).getFormattedRoad());
        viewHolder.distance.setText(geocodeResultList.get(position).getFormattedDistance());
        return convertView;
    }

    class ViewHolder{
        @Bind(R.id.map_search_address_item_tv)
        TextView address;
        @Bind(R.id.map_search_road_item_tv)
        TextView road;
        @Bind(R.id.map_search_distance_item_tv)
        TextView distance;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
