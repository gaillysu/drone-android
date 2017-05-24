package com.dayton.drone.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.MapSearchAdapter;
import com.dayton.drone.map.BaseMap;
import com.dayton.drone.map.builder.MapBuilder;
import com.dayton.drone.network.request.GetGeocodeRequest;
import com.dayton.drone.network.request.GetRouteMapRequest;
import com.dayton.drone.network.response.model.GeocodeResult;
import com.dayton.drone.network.response.model.GetGeocodeModel;
import com.dayton.drone.network.response.model.GetRouteMapModel;
import com.dayton.drone.network.response.model.Route;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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

    @Bind(R.id.navigation_search_layout)
    LinearLayout navigationSearchLayout;

    @Bind(R.id.navigation_operation_layout)
    LinearLayout navigationOperationLayout;

    @Bind(R.id.navigation_duration_tv)
    TextView navigation_duration_tv;

    @Bind(R.id.navigation_distance_tv)
    TextView navigation_distance_tv;

    @Bind(R.id.navigation_timer_tv)
    TextView navigation_timer_tv;

    @Bind(R.id.navigation_route1_button)
    Button route1;
    @Bind(R.id.navigation_route2_button)
    Button route2;
    @Bind(R.id.navigation_route3_button)
    Button route3;
    @Bind(R.id.navigation_start_stop_button)
    Button startStopNavigation;
    @Bind(R.id.my_toolbar)
    Toolbar toolbar;

    boolean navigationOnGoing = false;
    int currentRoute = 0;

    List<GeocodeResult> geocodeResults = new ArrayList<>();
    Route[] routeMap;
    Timer timer;
    long navigationDurationInSeconds;

    private BaseMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        initToolbar();
        map = new MapBuilder(mapLayout,this).build(savedInstanceState);
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                final GetGeocodeRequest getGeocodeRequest = new GetGeocodeRequest(v.getText() + "",getModel().getRetrofitManager().getGoogleMapApiKey());
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
                        geocodeResults = Arrays.asList(getGeocodeModel.getResults());
                        for(GeocodeResult result:geocodeResults) {
                            requestRouteMap(result.getGeometry().getLocation().getLat(),
                                    result.getGeometry().getLocation().getLng(),true);
                        }
                    }
                });
                return false;
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigationSearchLayout.setVisibility(View.GONE);
                navigationOperationLayout.setVisibility(View.VISIBLE);
                showAnimation(navigationOperationLayout);
                requestRouteMap(geocodeResults.get(position).getGeometry().getLocation().getLat(),
                        geocodeResults.get(position).getGeometry().getLocation().getLng(),false);
            }
        });
     }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.toolbar_title_tv);
        title.setText(getString(R.string.map_navigation_title));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnClick(R.id.navigation_back_to_search_image_button)
    public void back2Search(){
        navigationOperationLayout.setVisibility(View.GONE);
        navigationSearchLayout.setVisibility(View.VISIBLE);
        showAnimation(navigationSearchLayout);
    }

    @OnClick(R.id.navigation_route1_button)
    public  void onClickRout1() {
        showRouteInfomation(0);
    }
    @OnClick(R.id.navigation_route2_button)
    public  void onClickRout2() {
        showRouteInfomation(1);
    }
    @OnClick(R.id.navigation_route3_button)
    public  void onClickRout3() {
        showRouteInfomation(2);
    }
    @OnClick(R.id.navigation_start_stop_button)
    public  void onStartNavigation() {
        if(!navigationOnGoing) {
            getModel().getSyncController().startNavigation(map.getLocalLocation().getLatitude(),map.getLocalLocation().getLongitude(),searchEditText.getText().toString());
            navigationOnGoing = true;
            startStopNavigation.setText(R.string.navigation_stop);
            navigation_timer_tv.setVisibility(View.VISIBLE);
            navigation_duration_tv.setVisibility(View.INVISIBLE);
            navigation_distance_tv.setVisibility(View.INVISIBLE);
            timer = new Timer();
            navigationDurationInSeconds = 0;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    navigation_timer_tv.post(new Runnable() {
                        @Override
                        public void run() {
                            navigationDurationInSeconds = navigationDurationInSeconds + 1;
                            navigation_timer_tv.setText(formatNavigationDuration(navigationDurationInSeconds));
                            if(navigationDurationInSeconds%5==0) {
                                getModel().getSyncController().updateNavigation(map.getLocalLocation().getLatitude(),map.getLocalLocation().getLongitude(),routeMap[0].getLegs()[0].getDistance().getValue());
                            }
                        }
                    });
                }
            }, 0, 1000);
        }
        else {
            getModel().getSyncController().stopNavigation();
            navigationOnGoing = false;
            timer.cancel();
            startStopNavigation.setText(R.string.navigation_start);
            navigation_timer_tv.setVisibility(View.INVISIBLE);
            navigation_duration_tv.setVisibility(View.VISIBLE);
            navigation_distance_tv.setVisibility(View.VISIBLE);
        }
    }

    private void requestRouteMap(final double destinationLatitude,final double destinationLongitude,final boolean getDistance) {
        GetRouteMapRequest getRouteMapRequest = new GetRouteMapRequest(map.getLocalLocation().getLatitude(),
                map.getLocalLocation().getLongitude(),
                destinationLatitude,destinationLongitude,
                getString(R.string.map_navigation_mode),
                getModel().getRetrofitManager().getGoogleMapApiKey());

        getModel().getRetrofitManager().executeGoogleMapApi(getRouteMapRequest, new RequestListener<GetRouteMapModel>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }
            @Override
            public void onRequestSuccess(GetRouteMapModel getRouteMapModel) {
                routeMap = getRouteMapModel.getRoutes();
                if(getDistance) {
                    for(GeocodeResult result:geocodeResults) {
                        if(result.getGeometry().getLocation().getLat() == destinationLatitude &&
                                result.getGeometry().getLocation().getLng() == destinationLongitude)
                        {
                            result.setFormattedDistance(getRouteMapModel.getRoutes()[0].getLegs()[0].getDistance().getText());
                        }
                    }
                    searchListView.setAdapter(new MapSearchAdapter(NavigationActivity.this, geocodeResults));
                }
                else {
                    if(getRouteMapModel.getRoutes().length==0) {
                        return;
                    }
                    if(getRouteMapModel.getRoutes().length==2) {
                        route2.setVisibility(View.VISIBLE);
                    }
                    if(getRouteMapModel.getRoutes().length>=3) {
                        route2.setVisibility(View.VISIBLE);
                        route3.setVisibility(View.VISIBLE);
                    }
                    renderRouteMap(getRouteMapModel.getRoutes());
                    showRouteInfomation(0);
                }
            }
        });
    }

    private String formatNavigationDuration(long durationInSeconds)
    {
        String formattedDuration = String.format("%02d:%02d:%02d", (durationInSeconds)/3600, ((durationInSeconds)%3600)/60, ((durationInSeconds)%60));
        return formattedDuration;
    }

    private void renderRouteMap(Route[] routes)
    {
        map.renderRouteMap(routes);
    }
    private void showRouteInfomation(int route) {
        currentRoute = route;
        if(routeMap[currentRoute].getLegs().length>0) {
            navigation_duration_tv.setText(routeMap[currentRoute].getLegs()[0].getDuration().getText());
            navigation_distance_tv.setText(routeMap[currentRoute].getLegs()[0].getDistance().getText());
        }
    }
    private void showAnimation(View view)
    {
        TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                0, Animation.RELATIVE_TO_SELF, 1,
                Animation.RELATIVE_TO_SELF, 0);
        trans.setDuration(200);
        trans.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(trans);
    }

}
