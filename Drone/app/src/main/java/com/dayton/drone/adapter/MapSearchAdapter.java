package com.dayton.drone.adapter;

import android.content.Context;
import android.location.Address;
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
        TextView addrerss = (TextView) convertView.findViewById(R.id.map_search_adapter_item_tv);
        addrerss.setText(addresses.get(position).getFormatted_address());
        return convertView;
    }
}
