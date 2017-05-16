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

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.MapSearchAdapter;
import com.dayton.drone.map.BaseMap;
import com.dayton.drone.map.builder.MapBuilder;
import com.dayton.drone.map.listener.ResponseListener;
import com.dayton.drone.map.request.GeoRequest;

import java.util.ArrayList;
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
                map.searchPOI(new GeoRequest(v.getText() + "", new ResponseListener<List<Address>>() {

                    @Override
                    public void onSuccess(final List<Address> result) {
                        searchListView.post(new Runnable() {
                            @Override
                            public void run() {
                                searchListView.setAdapter(new MapSearchAdapter(NavigationActivity.this,result));
                            }
                        });

                    }
                    @Override
                    public void onError(Exception error) {
                        final List<Address> addresses = new ArrayList<Address>();
                        Address address = new Address(Locale.getDefault());
                        address.setLocality(error.getLocalizedMessage());
                        addresses.add(address);
                        searchListView.post(new Runnable() {
                            @Override
                            public void run() {
                                searchListView.setAdapter(new MapSearchAdapter(NavigationActivity.this,addresses));
                            }
                        });
                    }

                }));
                return false;
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
     }

}
