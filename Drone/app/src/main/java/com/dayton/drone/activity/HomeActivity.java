package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.MyHomeMenuAdapter;
import com.dayton.drone.bean.MenuBean;
import com.dayton.drone.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by boy on 2016/4/21.
 */
public class HomeActivity extends BaseActivity {

    @Bind(R.id.activity_home_meun_listview)
    ListView homeMenuListView;

    private List<MenuBean> listData;
    private int[] homeMenuIconArray ={R.mipmap.mainmenu_activity_icon,
            R.mipmap.mainmenu_sleep_icon,R.mipmap.mainmenu_gallery_icon,
            R.mipmap.mainmenu_setting_icon,R.mipmap.mainmenu_worldclock_icon};
    private String[] homeMenuTextArray;
    private final String activities = "ACTIVITIES";
    private final String sleep = "SLEEP";
    private final String clock = "WORLD\nCLOCK";
    private final String setting = "SETTINGS";
    private final String gallery = "GALLERY";
    private int type = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initData();
        homeMenuListView.setAdapter(new MyHomeMenuAdapter(listData));
        homeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content = homeMenuTextArray[position];
                switch (content) {
                    case activities:
                        type = 1;
                        openFragmentManage(1);
                        break;
                    case sleep:
                        type = 2;
                        openFragmentManage(2);
                        break;
                    case clock:
                        type = 3;
                        openFragmentManage(3);
                        break;
                    case setting:
                        type = 4;
                        openFragmentManage(4);
                        break;
                    case gallery:
                        type = 5;
                        openFragmentManage(5);
                        break;
                }
            }
        });
    }

    private void initData() {
        listData = new ArrayList<>(homeMenuIconArray.length);
        homeMenuTextArray = UIUtils.getStringArray(R.array.home_menu_text_data);
        for(int i=0;i< homeMenuIconArray.length;i++){
            MenuBean bean = new MenuBean();
            bean.setIconId(homeMenuIconArray[i]);
            bean.setDec(homeMenuTextArray[i]);
            listData.add(bean);
        }
    }


    public void openFragmentManage(int type){
        Intent intent = new Intent(this ,ManagerHomeFragmentActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            System.exit(0);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
