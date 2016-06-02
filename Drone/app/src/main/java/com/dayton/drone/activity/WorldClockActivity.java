package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.WorldClockAdapter;
import com.dayton.drone.database.entry.WorldClockDatabaseHelper;
import com.dayton.drone.event.TimerEvent;
import com.dayton.drone.event.WorldClockChangedEvent;
import com.dayton.drone.model.WorldClock;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/24.
 */
public class WorldClockActivity extends BaseActivity {
    @Bind(R.id.world_clock_date_tv)
    TextView dateTv;
    @Bind(R.id.world_clock_select_city_list)
    ListView worldClockListView;


    private List<WorldClock> listData;
    private WorldClockAdapter worldClockAdapter;
    private boolean isShowEditIcon = true;
    private int requestCode = 3 >> 2;
    private WorldClockDatabaseHelper worldClockDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_clock);
        ButterKnife.bind(this);
        worldClockDatabase = getModel().getWorldClockDatabaseHelper();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onEvent(final TimerEvent event) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                worldClockAdapter.notifyDataSetChanged();
            }
        });
    }

    public void initData() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = format.format(date);
        String[] currentTimeArray = currentTime.split("-");

        dateTv.setText(new SimpleDateFormat("MMM").format(date)+ currentTimeArray[2] + ", " + currentTimeArray[0]);

        listData = worldClockDatabase.getSelected();
        setListAdapter(isShowEditIcon);
        worldClockAdapter.onDeleteItemListener(new WorldClockAdapter.DeleteItemInterface() {
            @Override
            public void deleteItem(int position) {
                if(worldClockDatabase.update(listData.get(position),false))
                {
                    listData.remove(position);
                    worldClockAdapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new WorldClockChangedEvent(worldClockDatabase.getSelected()));
                }
            }
        });

    }

    @OnClick(R.id.world_clock_back_icon_ib)
    public void backClick() {
        finish();
    }

//    @OnClick(R.id.world_clock_date_edit_bt)
//    public void editButtonClick() {
//        isShowEditIcon = !isShowEditIcon;
//        setListAdapter(isShowEditIcon);
//        worldClockAdapter.notifyDataSetChanged();
//    }

    @OnClick(R.id.world_clock_add_city_iv)
    public void addCityClick() {
        Intent intent = new Intent(this, ChooseCityActivity.class);
        startActivityForResult(intent, requestCode);

    }

    public void setListAdapter(boolean isShow) {
        worldClockAdapter = new WorldClockAdapter(listData, this, isShow);
        worldClockListView.setAdapter(worldClockAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode) {
            boolean flag = data.getBooleanExtra("isChooseFlag", true);
            if (flag == true) {
               String timeZoneName =data.getStringExtra("worldClock");
                if(worldClockDatabase.getSelected().size()==5)
                {
                    Toast.makeText(WorldClockActivity.this,"Max 5 clock limited", Toast.LENGTH_LONG).show();
                    return;
                }
                WorldClock worldClock=  new WorldClock();
                worldClock.setTimeZoneName(timeZoneName);

                if(worldClockDatabase.update(worldClock,true))
                {
                    listData.add(worldClock);
                    worldClockAdapter.notifyDataSetChanged();
                    EventBus.getDefault().post(new WorldClockChangedEvent(worldClockDatabase.getSelected()));
                }
                else
                {
                    Toast.makeText(WorldClockActivity.this,"Failed,Clock existed", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
