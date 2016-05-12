package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;

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
    @Bind(R.id.choose_activity_cancel_bt)
    Button cancelButton;
    private boolean isChooseCity = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_add_city_layout);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.choose_activity_cancel_bt)
    public void cancelClick() {
        isChooseCity = false;
        back(isChooseCity);
    }

    private void back(boolean flag) {
        Intent intent = getIntent();
        intent.putExtra("isChooseFlag", flag);
//        ChooseCityBean cityBean =new ChooseCityBean();
//        intent.putExtra("chooseBean",cityBean);6217 9952 0002 6461 365
        setResult(0, intent);
        finish();
    }
}
