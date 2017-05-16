package com.dayton.drone.view;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.dayton.drone.R;

public class GoogleMapView extends LinearLayout {

    public GoogleMapView(Context context, OnMapReadyCallback onMapReadyCallback) {
        super(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.layout_google_map, null, false);
        addView(view,new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        MapFragment mapFragment = (MapFragment) ((FragmentActivity) context).getFragmentManager().findFragmentById(R.id.googlemap_fragment);
        mapFragment.getMapAsync(onMapReadyCallback);
    }
}
