package com.dayton.drone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.network.response.model.Address_Component;
import com.dayton.drone.network.response.model.GeocodeResult;

import java.util.ArrayList;
import java.util.Arrays;
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

        TextView addrerss = (TextView) convertView.findViewById(R.id.map_search_address_item_tv);
        TextView road = (TextView) convertView.findViewById(R.id.map_search_road_item_tv);
        TextView distance = (TextView) convertView.findViewById(R.id.map_search_distance_item_tv);
        formatAddress(addresses.get(position));
        addrerss.setText(addresses.get(position).getFormattedCityRegion());
        road.setText(addresses.get(position).getFormattedRoad());
        distance.setText(addresses.get(position).getFormattedDistance());
        return convertView;
    }

    private String getComponentAddress(Address_Component[] address_components,String type)
    {
        for(Address_Component address_component:address_components)
        {
            if(Arrays.asList(address_component.getTypes()).contains(type))
            {
                return address_component.getLong_name();
            }
        }
        return null;
    }
    private void formatAddress(GeocodeResult result)
    {
        String  address = null;
        //city region address
        if((address = getComponentAddress(result.getAddress_components(),context.getString(R.string.address_type_1)))!=null)
        {
            result.setFormattedCityRegion(address);
        }
        if((address = getComponentAddress(result.getAddress_components(),context.getString(R.string.address_type_2)))!=null)
        {
            result.setFormattedCityRegion(result.getFormattedCityRegion()+","+address);
        }
        if((address = getComponentAddress(result.getAddress_components(),context.getString(R.string.address_type_3)))!=null)
        {
            result.setFormattedCityRegion(result.getFormattedCityRegion()+","+address);
        }
        if((address = getComponentAddress(result.getAddress_components(),context.getString(R.string.address_type_4)))!=null)
        {
            result.setFormattedCityRegion(result.getFormattedCityRegion()+","+address);
        }
        //road address
        if((address = getComponentAddress(result.getAddress_components(),context.getString(R.string.address_type_5)))!=null)
        {
            result.setFormattedRoad(address);
        }
        if((address = getComponentAddress(result.getAddress_components(),context.getString(R.string.address_type_6)))!=null)
        {
            result.setFormattedRoad(result.getFormattedRoad()+" "+address);
        }
        if((address = getComponentAddress(result.getAddress_components(),context.getString(R.string.address_type_7)))!=null)
        {
            result.setFormattedRoad(result.getFormattedRoad()+","+address);
        }
    }
}
