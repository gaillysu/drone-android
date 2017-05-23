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
            convertView.setTag(new ViewHolder(convertView));
        }
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        formatAddress(addresses.get(position));
        viewHolder.addrerss.setText(addresses.get(position).getFormattedCityRegion());
        viewHolder.road.setText(addresses.get(position).getFormattedRoad());
        viewHolder.distance.setText(addresses.get(position).getFormattedDistance());
        return convertView;
    }

    private String getComponentAddress(AddressComponent[] address_components, String type)
    {
        for(AddressComponent address_component:address_components)
        {
            if(Arrays.asList(address_component.getTypes()).contains(type))
            {
                return address_component.getLongName();
            }
        }
        return null;
    }
    private void formatAddress(GeocodeResult result)
    {
        String  address = null;
        //city region address
        if((address = getComponentAddress(result.getAddressComponents(),context.getString(R.string.address_type_1)))!=null)
        {
            result.setFormattedCityRegion(address);
        }
        if((address = getComponentAddress(result.getAddressComponents(),context.getString(R.string.address_type_2)))!=null)
        {
            result.setFormattedCityRegion(result.getFormattedCityRegion()+","+address);
        }
        if((address = getComponentAddress(result.getAddressComponents(),context.getString(R.string.address_type_3)))!=null)
        {
            result.setFormattedCityRegion(result.getFormattedCityRegion()+","+address);
        }
        if((address = getComponentAddress(result.getAddressComponents(),context.getString(R.string.address_type_4)))!=null)
        {
            result.setFormattedCityRegion(result.getFormattedCityRegion()+","+address);
        }
        //road address
        if((address = getComponentAddress(result.getAddressComponents(),context.getString(R.string.address_type_5)))!=null)
        {
            result.setFormattedRoad(address);
        }
        if((address = getComponentAddress(result.getAddressComponents(),context.getString(R.string.address_type_6)))!=null)
        {
            result.setFormattedRoad(result.getFormattedRoad()+" "+address);
        }
        if((address = getComponentAddress(result.getAddressComponents(),context.getString(R.string.address_type_7)))!=null)
        {
            result.setFormattedRoad(result.getFormattedRoad()+","+address);
        }
    }

    class ViewHolder{
        @Bind(R.id.map_search_address_item_tv)
        TextView addrerss;
        @Bind(R.id.map_search_road_item_tv)
        TextView road;
        @Bind(R.id.map_search_distance_item_tv)
        TextView distance;
        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
