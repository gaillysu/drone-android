package com.dayton.drone.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.MapSearchAdapter;
import com.dayton.drone.adapter.PlaceAutocompleteAdapter;
import com.dayton.drone.event.PlaceChangedEvent;
import com.dayton.drone.map.BaseMap;
import com.dayton.drone.map.builder.MapBuilder;
import com.dayton.drone.network.request.GetGeocodeRequest;
import com.dayton.drone.network.request.GetRouteMapRequest;
import com.dayton.drone.network.response.model.GeocodeResult;
import com.dayton.drone.network.response.model.Geometry;
import com.dayton.drone.network.response.model.GetGeocodeModel;
import com.dayton.drone.network.response.model.GetRouteMapModel;
import com.dayton.drone.network.response.model.Location;
import com.dayton.drone.network.response.model.Route;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by med on 17/5/15.
 */

public class NavigationActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,RadioGroup.OnCheckedChangeListener{

    private static final String TAG = "NavigationActivity";

    @Bind(R.id.map_content)
    RelativeLayout mapLayout;

    @Bind(R.id.address_search_edit_text)
    AutoCompleteTextView searchEditText;

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

    @Bind(R.id.navigation_mode_radiogroup)
    RadioGroup navigation_mode_radiogroup;
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

    private static final int GOOGLE_API_CLIENT_ID = 0;
    protected GoogleApiClient mGoogleApiClient;
    private PlaceAutocompleteAdapter mAdapter;

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
                final GetGeocodeRequest getGeocodeRequest = new GetGeocodeRequest(v.getText() + "",getModel().getRetrofitManager().getGoogleMapApiKey(), Locale.getDefault().getLanguage());
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
                                    result.getGeometry().getLocation().getLng(),true,getString(R.string.map_navigation_mode_walking));
                        }
                    }
                });
                hideKeyboard(searchEditText);
                return true;
            }
        });

        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(routeMap.length==0) {
                    return;
                }
                navigationSearchLayout.setVisibility(View.GONE);
                navigationOperationLayout.setVisibility(View.VISIBLE);
                showAnimation(navigationOperationLayout);
                renderRouteMap(routeMap);
                showRouteInfomation(0);
            }
        });


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID,this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        searchEditText.setOnItemClickListener(autocompleteClickListener);
        mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, null, null);
        searchEditText.setAdapter(mAdapter);
        navigation_mode_radiogroup.setOnCheckedChangeListener(this);
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

    private void requestRouteMap(final double destinationLatitude,final double destinationLongitude,final boolean getDistance,String mode) {
        GetRouteMapRequest getRouteMapRequest = new GetRouteMapRequest(map.getLocalLocation().getLatitude(),
                map.getLocalLocation().getLongitude(),
                destinationLatitude,destinationLongitude,
                mode,
                getModel().getRetrofitManager().getGoogleMapApiKey());

        getModel().getRetrofitManager().executeGoogleMapApi(getRouteMapRequest, new RequestListener<GetRouteMapModel>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }
            @Override
            public void onRequestSuccess(GetRouteMapModel getRouteMapModel) {
                routeMap = getRouteMapModel.getRoutes();
                if(getRouteMapModel.getRoutes().length==0) {
                    Toast.makeText(NavigationActivity.this,R.string.route_no_found_message,Toast.LENGTH_LONG).show();
                    return;
                }
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode() + "," + connectionResult.getErrorMessage());
    }
    private AdapterView.OnItemClickListener autocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(updatePlaceDetailsCallback);
            Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> updatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);
            Log.i(TAG, "Place details received,name: " + place.getName() + ",id: " + place.getId() + ",address: "+ place.getAddress() + ",latlng: " + place.getLatLng());
            Location location = new Location();
            location.setLat(place.getLatLng().latitude);
            location.setLng(place.getLatLng().longitude);
            startRoute(location,place.getAddress()+"",place.getName()+"");
            places.release();
        }
    };

    private void startRoute(Location location,String address,String name)
    {
        hideKeyboard(searchEditText);
        geocodeResults = new ArrayList<>();
        GeocodeResult geocodeResult = new GeocodeResult();
        Geometry geometry = new Geometry();
        geometry.setLocation(location);
        geocodeResult.setGeometry(geometry);
        geocodeResult.setFormattedCityRegion(address);
        geocodeResult.setFormattedRoad(name);
        geocodeResults.add(geocodeResult);
        requestRouteMap(location.getLat(),location.getLng(),true,getString(R.string.map_navigation_mode_walking));
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        String mode = getString(R.string.map_navigation_mode_walking);
        if(checkedId == R.id.navigation_walking_button)
        {
            mode = getString(R.string.map_navigation_mode_walking);
        }
        if(checkedId == R.id.navigation_driving_button)
        {
            mode = getString(R.string.map_navigation_mode_driving);
        }
        if(checkedId == R.id.navigation_transit_button)
        {
            mode = getString(R.string.map_navigation_mode_transit);
        }
        requestRouteMap(geocodeResults.get(0).getGeometry().getLocation().getLat(),
                geocodeResults.get(0).getGeometry().getLocation().getLng(),false,mode);
    }
}
