package com.dayton.drone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.activity.tutorial.SelectDeviceActivity;
import com.dayton.drone.adapter.AddWatchMenuAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by med on 16/5/17.
 */
public class AddWatchActivity extends BaseActivity {

    @Bind(R.id.activity_addwatch_menu_listview)
    ListView addwatchMenuListview;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addwatch);
        ButterKnife.bind(this);
        List<String> listMenu = new ArrayList<String>();
        listMenu.add("Contacts Notifications");
        listMenu.add("Forget this watch");
        addwatchMenuListview.setAdapter(new AddWatchMenuAdapter(listMenu,this));
        addwatchMenuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0) {
                    //TODO  retrive phonebook to get all contacts
                }
                else if(position==1) {
                    getModel().getSyncController().forgetDevice();
                }
            }
        });
    }

    @OnClick(R.id.activity_addwatch_back_imagebutton)
    public void back()
    {
        finish();
    }

    @OnClick(R.id.activity_addwatch_add_imagebutton)
    public void addWatch()
    {
        startActivity(SelectDeviceActivity.class);
    }
}
