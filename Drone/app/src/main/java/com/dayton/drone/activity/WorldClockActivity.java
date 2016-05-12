package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.WorldClockAdapter;
import com.dayton.drone.database.entry.WorldClockDatabaseHelper;
import com.dayton.drone.modle.WorldClock;

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
    @Bind(R.id.world_clock_date_edit_bt)
    ImageButton editButton;
    @Bind(R.id.world_clock_add_city_iv)
    ImageButton addCityButton;
    @Bind(R.id.world_clock_date_tv)
    TextView dateTv;
    @Bind(R.id.world_clock_select_city_list)
    ListView worldClockListView;
    @Bind(R.id.content_title_dec_tv)
    TextView titleTextView;

    private List<WorldClock> listData;
    private WorldClockAdapter worldClockAdapter;
    private boolean isShowEditIcon = true;
    private int requestCode = 3>>2;
    private WorldClockDatabaseHelper worldClockDatabase;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_clock);
        ButterKnife.bind(this);
//        listData = new ArrayList<>(3);
        titleTextView.setText(getString(R.string.world_clock_title_text));
        worldClockDatabase = getModel().getWorldClockDatabaseHelper();
        initData();
    }


    public void initData() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime = format.format(date);
        String[] currentTimeArray = currentTime.split("-");
        dateTv.setText(getModel().getString(R.string.main_table_date) + " "
                + currentTimeArray[1] + ", " + currentTimeArray[0]);

//        Calendar calendar = Calendar.getInstance();
//        int currentHours = calendar.get(Calendar.HOUR_OF_DAY);
//        TimeZone timeZone = calendar.getTimeZone();
//
//        WorldClockListBean bean = new WorldClockListBean();
//        bean.setCity("London");
//        bean.setModernDate("Today");
//        bean.setTimeDifference("7 hours Behind");
//        bean.setCityCurrentTime(currentHours + 7 + " PM");
//        listData.add(bean);
//
//        WorldClockListBean bean1 = new WorldClockListBean();
//        bean1.setCity("Paris");
//        bean1.setModernDate("Today");
//        bean1.setTimeDifference("6 hours Behind");
//        bean1.setCityCurrentTime(currentHours + 6 + " PM");
//        listData.add(bean1);
//
//        WorldClockListBean bean2 = new WorldClockListBean();
//        bean2.setCity("New York");
//        bean2.setModernDate("Today");
//        bean2.setTimeDifference("12 hours Behind");
//        bean2.setCityCurrentTime(currentHours + 12 + " PM");
//        listData.add(bean2);
//
//        WorldClockListBean bean3 = new WorldClockListBean();
//        bean3.setCity("Shanghai");
//        bean3.setModernDate("Today");
//        bean3.setTimeDifference("7 hours Behind");
//        bean3.setCityCurrentTime(currentHours + 7 + " PM");
//        listData.add(bean3);
        listData = worldClockDatabase.getSelected();
        setListAdapter(isShowEditIcon);
        worldClockAdapter.onDeleteItemListener(new WorldClockAdapter.DeleteItemInterface() {
            @Override
            public void deleteItem(int position) {
                listData.remove(position);
                worldClockAdapter.notifyDataSetChanged();
            }
        });

    }

    @OnClick(R.id.content_title_back_bt)
    public void backClick() {
        finish();
    }

    @OnClick(R.id.world_clock_date_edit_bt)
    public void editButtonClick() {
        isShowEditIcon = !isShowEditIcon;
        setListAdapter(isShowEditIcon);
        worldClockAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.world_clock_add_city_iv)
    public void addCityClick() {
        Intent intent = new Intent(this,ChooseCityActivity.class);
        startActivityForResult(intent ,requestCode);

    }

    public void setListAdapter(boolean  isShow){
        worldClockAdapter = new WorldClockAdapter(listData,this ,isShow);
        worldClockListView.setAdapter(worldClockAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == this.requestCode){
           boolean flag= data.getBooleanExtra("isChooseFlag",true);
            if(flag == true) {
                //ToDO
            }
            Toast.makeText(this ,"没有选择的返回" ,Toast.LENGTH_SHORT).show();
        }
    }
}
