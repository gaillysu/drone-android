package com.dayton.drone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.ChooseCityActivity;
import com.dayton.drone.adapter.WorldClockAdapter;
import com.dayton.drone.application.ApplicationModel;
import com.dayton.drone.event.CityNumberChangedEvent;
import com.dayton.drone.event.Timer10sEvent;
import com.dayton.drone.event.WorldClockChangedEvent;
import com.dayton.drone.utils.WeatherUtils;
import com.dayton.drone.view.ListViewCompat;
import com.dayton.drone.viewmodel.WorldClockViewModel;

import net.medcorp.library.worldclock.City;
import net.medcorp.library.worldclock.event.WorldClockInitializeEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jason on 2017/4/27.
 */

public class WorldClockMainFragment extends Fragment {

    @Bind(R.id.world_clock_date_tv)
    TextView dateTv;
    @Bind(R.id.world_clock_localhost_city)
    TextView localCity;
    @Bind(R.id.world_clock_item_city_time)
    TextView localTime;
    @Bind(R.id.world_clock_select_city_list)
    ListViewCompat worldClockListView;

    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    private List<WorldClockViewModel> listData;
    private WorldClockAdapter worldClockAdapter;
    private int requestCode = 3 >> 2;
    private ApplicationModel mApplicationModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.world_clock_main_fragment_layout, container, false);
        ButterKnife.bind(this, view);
        listData = new ArrayList<>();
        setHasOptionsMenu(true);
        mApplicationModel = (ApplicationModel) getActivity().getApplication();
        initLocalDateTime();
        initData();
        return view;
    }

    @Subscribe
    public void onEvent(final Timer10sEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                initLocalDateTime();
                worldClockAdapter.notifyDataSetChanged();
            }
        });
    }


    private void initLocalDateTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String currentTime = format.format(date);
        String[] currentTimeArray = currentTime.split("-");
        dateTv.setText(currentTimeArray[2] + " " + new SimpleDateFormat("MMM").format(date) + " " + currentTimeArray[0]);
        format = new SimpleDateFormat(FORMAT_LONG);
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = calendar.getTimeZone();
        String timeName = timeZone.getID().split("/")[1].replace("_", " ");
        localCity.setText(timeName);
        String[] localTimeStr = format.format(calendar.getTime()).split(" ");
        if (new Integer(localTimeStr[1].split(":")[0]).intValue() <= 12) {

            localTime.setText(localTimeStr[1].split(":")[0] + ":" + localTimeStr[1].split(":")[1] + " AM");
        } else {
            localTime.setText(localTimeStr[1].split(":")[0] + ":" + localTimeStr[1].split(":")[1] + " PM");
        }
    }

    public void initData() {
        worldClockAdapter = new WorldClockAdapter(listData, WorldClockMainFragment.this.getActivity());
        worldClockListView.setAdapter(worldClockAdapter);
        worldClockAdapter.onDeleteItemListener(deleteItemInterface);
        refreshList();

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.toolbar_add_button).setVisible(true);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.toolbar_add_button){
            Intent intent = new Intent(WorldClockMainFragment.this.getActivity(), ChooseCityActivity.class);
            startActivityForResult(intent, requestCode);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode) {
            if (data != null) {
                boolean flag = data.getBooleanExtra(getString(R.string.is_choose_flag), false);
                if (flag) {
                    refreshList();
                    EventBus.getDefault().post(new WorldClockChangedEvent());
                }
            }
        }
    }

    private WorldClockAdapter.DeleteItemInterface deleteItemInterface = new WorldClockAdapter.DeleteItemInterface() {
        @Override
        public void deleteItem(int position) {
            int id = listData.get(position).getCityId();
            City city = mApplicationModel.getWorldClockDatabaseHelp().get(id);
            if (city != null) {
                listData.remove(position);
                worldClockAdapter.notifyDataSetChanged();
                city.setSelected(false);
                mApplicationModel.getWorldClockDatabaseHelp().update(city);
                refreshList();
            } else {
                Log.w("Karl", "something really bad is wrong!");
            }
        }
    };

    private void refreshList() {
        listData.clear();
        WeatherUtils.removeAllCities(WorldClockMainFragment.this.getActivity());
        WeatherUtils.addWeatherCity(WorldClockMainFragment.this.getActivity(), localCity.getText().toString());
        List<City> selectedCities = mApplicationModel.getWorldClockDatabaseHelp().getSelect();
        for (City city : selectedCities) {
            listData.add(new WorldClockViewModel(city));
            worldClockAdapter.notifyDataSetChanged();
            WeatherUtils.addWeatherCity(WorldClockMainFragment.this.getActivity(), city.getName());
        }
        Log.w("weather", "city list: " + WeatherUtils.getWeatherCities(WorldClockMainFragment.this.getActivity()).toString());
        EventBus.getDefault().post(new CityNumberChangedEvent());
    }

    @Subscribe
    public void onEvent(WorldClockInitializeEvent event) {
        if (event.getStatus() == WorldClockInitializeEvent.STATUS.FINISHED) {
            refreshList();
        }
    }
}
