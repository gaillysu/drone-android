package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.ChooseCityAdapter;
import com.dayton.drone.database.entry.WorldClockDatabaseHelper;
import com.dayton.drone.modle.WorldClock;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/12.
 */
public class ChooseCityActivity extends BaseActivity {

    @Bind(R.id.choose_activity_search_ib)
    ImageButton searchButton;
    @Bind(R.id.choose_activity_search_edit)
    EditText searchTextEditText;
    @Bind(R.id.choose_activity_city_list)
    ListView cityListView;
    private boolean isChooseCity = false;
    private List<WorldClock> worldClockDataList;
    private WorldClockDatabaseHelper worldClockDatabase;
    private ChooseCityAdapter cityAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_city_layout);
        ButterKnife.bind(this);
        worldClockDatabase = getModel().getWorldClockDatabaseHelper();
        worldClockDataList = worldClockDatabase.getAll();
        cityAdapter = new ChooseCityAdapter(this , worldClockDataList);
        cityListView.setAdapter(cityAdapter);
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position , long Id) {
                isChooseCity = true;
                WorldClock worldClock =  worldClockDataList.get(position);
                Intent intent = getIntent();
                intent.putExtra("isChooseFlag", isChooseCity);
                intent.putExtra("worldClock",worldClock);
                setResult(0, intent);
                finish();
            }
        });

    }

    @OnClick(R.id.choose_activity_cancel_bt)
    public void cancelClick() {
        isChooseCity = false;
        back(isChooseCity);
    }

    private void back(boolean flag) {
        Intent intent = getIntent();
        intent.putExtra("isChooseFlag", flag);
        setResult(0, intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            back(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
