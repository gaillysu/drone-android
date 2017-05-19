package com.dayton.drone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.network.response.model.GeocodeResult;

import java.util.List;

/**
 * Created by med on 17/5/16.
 */

public class MapSearchAdapter extends BaseAdapter {

    private Context context;
    List<GeocodeResult> addresses;

    public MapSearchAdapter(Context context,List<GeocodeResult> addresses) {
        this.context = context;
        this.addresses = addresses;
    }

    @Override
    public int getCount() {
        return addresses.size();
    }

    @Override
    public Object getItem(int position) {
        return addresses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.map_search_adapter_item, null);
        }
        //TODO here need format the output address by 'type'
        TextView addrerss = (TextView) convertView.findViewById(R.id.map_search_address_item_tv);
        addrerss.setText(addresses.get(position).getFormatted_address());
        TextView road = (TextView) convertView.findViewById(R.id.map_search_road_item_tv);
        road.setText(addresses.get(position).getAddress_components()[1].getShort_name());

        TextView distance = (TextView) convertView.findViewById(R.id.map_search_distance_item_tv);
        //TODO here need calculate the distance, below is a dummy value
        distance.setText("1000km");

        return convertView;
    }
}
