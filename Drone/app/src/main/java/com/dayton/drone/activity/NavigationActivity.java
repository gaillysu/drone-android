package com.dayton.drone.activity;

import android.database.DataSetObserver;
import android.location.Address;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.MapSearchAdapter;
import com.dayton.drone.map.BaseMap;
import com.dayton.drone.map.builder.MapBuilder;
import com.dayton.drone.map.listener.ResponseListener;
import com.dayton.drone.map.request.GeoRequest;
import com.dayton.drone.network.request.GetGeocodeRequest;
import com.dayton.drone.network.request.GetRouteMapRequest;
import com.dayton.drone.network.response.model.GeocodeResult;
import com.dayton.drone.network.response.model.GetGeocodeModel;
import com.dayton.drone.network.response.model.GetRouteMapModel;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by med on 17/5/15.
 */

public class NavigationActivity extends BaseActivity {

    @Bind(R.id.map_content)
    RelativeLayout mapLayout;

    @Bind(R.id.address_search_edit_text)
    EditText searchEditText;

    @Bind(R.id.address_search_list_view)
    ListView searchListView;

    List<GeocodeResult> geocodeResults = new ArrayList<>();

    private BaseMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        map = new MapBuilder(mapLayout,this).build(savedInstanceState);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                GetGeocodeRequest getGeocodeRequest = new GetGeocodeRequest(v.getText() + "",getModel().getRetrofitManager().getGoogleMapApiKey());
                getModel().getRetrofitManager().executeGoogleMapApi(getGeocodeRequest, new RequestListener<GetGeocodeModel>() {
                    @Override
                    public void onRequestFailure(final SpiceException spiceException) {
                        searchListView.post(new Runnable() {
                            @Override
                            public void run() {
                                searchListView.removeAllViews();
                                Toast.makeText(NavigationActivity.this,spiceException.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void onRequestSuccess(final GetGeocodeModel getGeocodeModel) {
                        searchListView.post(new Runnable() {
                            @Override
                            public void run() {
                                geocodeResults = Arrays.asList(getGeocodeModel.getResults());
                                searchListView.setAdapter(new MapSearchAdapter(NavigationActivity.this, geocodeResults));
                            }
                        });
                    }
                });
                return false;
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetRouteMapRequest getRouteMapRequest = new GetRouteMapRequest(map.getLocalLocation().getLatitude(),
                        map.getLocalLocation().getLongitude(),
                        geocodeResults.get(position).getGeometry().getLocation().getLat(),
                        geocodeResults.get(position).getGeometry().getLocation().getLng(),
                        getModel().getRetrofitManager().getGoogleMapApiKey());

                getModel().getRetrofitManager().executeGoogleMapApi(getRouteMapRequest, new RequestListener<GetRouteMapModel>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {

                    }
                    @Override
                    public void onRequestSuccess(GetRouteMapModel getRouteMapModel) {
                        map.renderRouteMap(getRouteMapModel.getRoutes());
                    }
                });

            }
        });
     }

    @OnClick(R.id.activity_navigation_back_imagebutton)
    public void back2MainMenu(){
        finish();
    }

}
