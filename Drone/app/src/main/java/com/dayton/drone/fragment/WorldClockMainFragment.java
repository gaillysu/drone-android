package com.dayton.drone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
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
import com.dayton.drone.utils.SpUtils;
import com.dayton.drone.utils.WeatherUtils;
import com.dayton.drone.viewmodel.DragListViewItem;
import com.dayton.drone.viewmodel.WorldClockCityItemModel;
import com.dayton.drone.viewmodel.WorldClockTitleModel;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

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
    @Bind(R.id.world_clock_select_city_list)
    DragListView mDragListView;
    private ArrayList<Pair<Integer, DragListViewItem>> listData;
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
        setData();
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
    }

    public void initData() {
        List<City> select = mApplicationModel.getWorldClockDatabaseHelp().getSelect();
        addLocalCity();
        addCity(select);
    }


    private void setData() {
        worldClockAdapter = new WorldClockAdapter(mApplicationModel, R.id.drag_item, listData, false);
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(false);
        mDragListView.setLayoutManager(new LinearLayoutManager(WorldClockMainFragment.this.getActivity()));
        mDragListView.setCanDragHorizontally(false);
        mDragListView.setCustomDragItem(null);
        mDragListView.setHorizontalFadingEdgeEnabled(false);
        mDragListView.setCanNotDragBelowBottomItem(false);
        mDragListView.setAdapter(worldClockAdapter, true);
        mDragListView.setDragListCallback(new DragListView.DragListCallback() {
            @Override
            public boolean canDragItemAtPosition(int dragPosition) {
                int itemViewType = worldClockAdapter.getItemViewType(dragPosition);
                if (dragPosition == 1) {
                    return false;
                }
                if (itemViewType == WorldClockAdapter.VIEW_TYPE_TITLE) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean canDropItemAtPosition(int dropPosition) {
                int itemViewType = worldClockAdapter.getItemViewType(3);
                if (itemViewType == WorldClockAdapter.VIEW_TYPE_TITLE) {
                    if (dropPosition == 0 | dropPosition == 1 | dropPosition == 2) {
                        return false;
                    }
                } else {
                    if (dropPosition == 0 | dropPosition == 1 | dropPosition == 2 | dropPosition == 3 | dropPosition == 4) {
                        return false;
                    }
                }
                return true;
            }
        });

        mDragListView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
            @Override
            public void onItemSwipeStarted(ListSwipeItem item) {
            }

            @Override
            public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
                Pair<Integer, DragListViewItem> adapterItem = (Pair<Integer, DragListViewItem>) item.getTag();
                int pos = mDragListView.getAdapter().getPositionForItem(adapterItem);
                if (pos != 1) {
                    int cityId = adapterItem.second.getItem().getCityId();
                    if (pos == 3) {
                        SpUtils.saveHomeCityId(WorldClockMainFragment.this.getActivity(), -1);
                    }
                    City city = mApplicationModel.getWorldClockDatabaseHelp().get(cityId);
                    city.setSelected(false);
                    mApplicationModel.getWorldClockDatabaseHelp().update(city);
                    mDragListView.getAdapter().removeItem(pos);
                    worldClockAdapter.notifyDataSetChanged();
                }
            }
        });

        mDragListView.setDragListListener(new DragListView.DragListListener() {
            @Override
            public void onItemDragStarted(int position) {

            }

            @Override
            public void onItemDragging(int itemPosition, float x, float y) {
            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                int itemViewType = worldClockAdapter.getItemViewType(3);
                if (itemViewType == WorldClockAdapter.VIEW_TYPE_ITEM) {
                    DragListViewItem second = listData.get(3).second;
                    SpUtils.saveHomeCityId(WorldClockMainFragment.this.getActivity(),
                            second.getItem().getCityId());
                }
            }
        });


    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.toolbar_add_button).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_add_button) {
            Intent intent = new Intent(WorldClockMainFragment.this.getActivity(), ChooseCityActivity.class);
            startActivityForResult(intent, requestCode);
            return true;
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

    private void refreshList() {
        listData.clear();
        WeatherUtils.removeAllCities(WorldClockMainFragment.this.getActivity());
        WeatherUtils.addWeatherCity(WorldClockMainFragment.this.getActivity(), Calendar.getInstance().getTimeZone().getID().split("/")[1].replace("_", " "));
        List<City> selectedCities = mApplicationModel.getWorldClockDatabaseHelp().getSelect();
        addLocalCity();
        addCity(selectedCities);
        worldClockAdapter.notifyDataSetChanged();
        Log.w("weather", "city list: " + WeatherUtils.getWeatherCities(WorldClockMainFragment.this.getActivity()).toString());
        EventBus.getDefault().post(new CityNumberChangedEvent());
    }

    private void addLocalCity() {
        Calendar calendar = Calendar.getInstance();
        TimeZone timeZone = calendar.getTimeZone();
        String timeName = timeZone.getID().split("/")[1].replace("_", " ");
        City localCity = mApplicationModel.getWorldClockDatabaseHelp().get(timeName);
        listData.add(new Pair<Integer, DragListViewItem>
                (0, new DragListViewItem(null, new WorldClockTitleModel(getString(R.string.world_clock_local_time)))));
        listData.add(new Pair<Integer, DragListViewItem>
                (1, new DragListViewItem(new WorldClockCityItemModel(localCity), null)));
    }


    private void addCity(List<City> selectedCities) {
        int homeCityId = SpUtils.getHomeCityId(WorldClockMainFragment.this.getContext());
        WorldClockTitleModel worldClockTitleModel = new WorldClockTitleModel(getString(R.string.world_clock_adapter_home_time));
        listData.add(new Pair<>(2, new DragListViewItem(null, worldClockTitleModel)));
        if (homeCityId != -1) {
            City city = mApplicationModel.getWorldClockDatabaseHelp().get(homeCityId);
            listData.add(new Pair<Integer, DragListViewItem>(3, new DragListViewItem(new WorldClockCityItemModel(city), null)));
            listData.add(new Pair<>(4, new DragListViewItem(null, new WorldClockTitleModel(getString(R.string.world_clock_adapter_world_time)))));
            for (int i = 0; i < selectedCities.size(); i++) {
                City worldClickCity = selectedCities.get(i);
                if (selectedCities.get(i).getId() == homeCityId) {
                    continue;
                }
                WorldClockCityItemModel model = new WorldClockCityItemModel(worldClickCity);
                listData.add(new Pair<>(i + 5, new DragListViewItem(model, null)));
            }
        } else {
            listData.add(new Pair<>(3, new DragListViewItem(null, new WorldClockTitleModel(getString(R.string.world_clock_adapter_world_time)))));
            for (int i = 0; i < selectedCities.size(); i++) {
                City worldClickCity = selectedCities.get(i);
                WorldClockCityItemModel model = new WorldClockCityItemModel(worldClickCity);
                listData.add(new Pair<>(i + 4, new DragListViewItem(model, null)));
            }
        }
    }

    @Subscribe
    public void onEvent(WorldClockInitializeEvent event) {
        if (event.getStatus() == WorldClockInitializeEvent.STATUS.FINISHED) {
            refreshList();
        }
    }
}
