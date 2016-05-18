package com.dayton.drone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.MyHomeMenuAdapter;
import com.dayton.drone.bean.MenuBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/21.
 */
public class HomeActivity extends BaseActivity {

    @Bind(R.id.activity_home_meun_listview)
    ListView homeMenuListView;

    private List<MenuBean> listData;


    private int[] homeMenuIconArray ={R.mipmap.mainmenu_activity_icon
            ,R.mipmap.mainmenu_worldclock_icon};

    private String[] homeMenuTextArray;
    private final String activities = "ACTIVITIES";
    private final String clock = "WORLD\nCLOCK";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initData();
        homeMenuListView.setAdapter(new MyHomeMenuAdapter(listData,this));
        homeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content = homeMenuTextArray[position];
                switch (content) {
                    case activities:
                        startActivity(ActivitiesActivity.class);
                        break;
                    case clock:
                        startActivity(WorldClockActivity.class);
                        break;
                }
            }
        });
    }

    private void initData() {
        listData = new ArrayList<>(homeMenuIconArray.length);
        homeMenuTextArray = getResources().getStringArray(R.array.home_menu_text_data);
        for(int i=0;i< homeMenuIconArray.length;i++){
            MenuBean bean = new MenuBean();
            bean.setIconId(homeMenuIconArray[i]);
            bean.setDec(homeMenuTextArray[i]);
            listData.add(bean);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(!getModel().getSyncController().isConnected())
        {
            getModel().getSyncController().startConnect(false);
        }
    }

    @OnClick(R.id.manager_fragment_title_right_add)
    public void addWatch()
    {
        startActivity(AddWatchActivity.class);
    }

    @OnClick(R.id.title_head_icon)
    public void startProfile(){
        startActivity(ProfileActivity.class);
    }
}
