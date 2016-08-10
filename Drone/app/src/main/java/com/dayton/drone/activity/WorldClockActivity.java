package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.WorldClockAdapter;
import com.dayton.drone.event.Timer10sEvent;
import com.dayton.drone.event.WorldClockChangedEvent;
import com.dayton.drone.view.ListViewCompat;
import com.dayton.drone.viewmodel.WorldClockViewModel;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by boy on 2016/4/24.
 */
public class WorldClockActivity extends BaseActivity {
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
    private Realm realm = Realm.getDefaultInstance();
    private int requestCode = 3 >> 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_clock);
        listData = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }
        ButterKnife.bind(this);
        initLocalDateTime();
        initData();
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
        dateTv.setText(currentTimeArray[2] + " " + new SimpleDateFormat("MMM", Locale.US).format(date) + " " + currentTimeArray[0]);
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
        worldClockAdapter = new WorldClockAdapter(listData, this);
        worldClockListView.setAdapter(worldClockAdapter);
        worldClockAdapter.onDeleteItemListener(deleteItemInterface);
        refreshList();

    }

    @OnClick(R.id.world_clock_back_icon_ib)
    public void backClick() {
        finish();
    }

    @OnClick(R.id.world_clock_add_city_iv)
    public void addCityClick() {
        Intent intent = new Intent(WorldClockActivity.this, ChooseCityActivity.class);
        startActivityForResult(intent, requestCode);
    }

    public void setListAdapter() {
        worldClockAdapter = new WorldClockAdapter(listData, this);
        worldClockListView.setAdapter(worldClockAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode) {
            if (data != null) {
                boolean flag = data.getBooleanExtra("isChooseFlag", false);
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
            RealmResults<City> cities = realm.where(City.class).equalTo("id", id).findAll();
            if (cities.size() == 1) {
                listData.remove(position);
                worldClockAdapter.notifyDataSetChanged();
                realm.beginTransaction();
                cities.get(0).setSelected(false);
                realm.commitTransaction();
                refreshList();
            } else {
                Log.w("Karl", "something really bad is wrong!");
            }
        }
    };

    private void refreshList() {
        listData.clear();
        RealmResults<City> selectedCities = realm.where(City.class).equalTo("selected", true).findAll();
        for (City city : selectedCities) {
            city.getOffSetFromGMT();
            listData.add(new WorldClockViewModel(city));
            worldClockAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onEvent(WorldClockInitializeEvent event) {
        if(event.getStatus() == WorldClockInitializeEvent.STATUS.FINISHED){
            refreshList();
        }
    }
}
