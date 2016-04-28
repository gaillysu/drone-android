package com.dayton.drone.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.dayton.drone.R;
import com.dayton.drone.adapter.MyHomeMenuAdapter;
import com.dayton.drone.bean.MenuBean;
import com.dayton.drone.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by boy on 2016/4/21.
 */
public class HomeActivity extends Activity {

    private List<MenuBean> mListData;
    private int[] mHomeMenuIconArray={R.mipmap.icon_04,
            R.mipmap.icon_04,R.mipmap.icon_04,
            R.mipmap.icon_04,R.mipmap.icon_04};
    private String[] mHomeMenuTextArray;
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
        initData();
        ListView listView = (ListView) findViewById(R.id.home_meun_list);
        listView.setAdapter(new MyHomeMenuAdapter(mListData));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String content = mHomeMenuTextArray[position];
                switch(content){
                    case activities:
                        type = 1;
                        opneFragmentManage(1);
                        break;
                    case sleep:
                        type = 2;
                        opneFragmentManage(2);
                        break;
                    case clock:
                        type = 3;
                        opneFragmentManage(3);
                        break;
                    case setting:
                        type = 4;
                        opneFragmentManage(4);
                        break;
                    case gallery:
                        type  =5;
                        opneFragmentManage(5);
                        break;
                }
            }
        });
    }

    private void initData() {
        mListData = new ArrayList<>(3);
        mHomeMenuTextArray = UIUtils.getStringArray(R.array.home_menu_text_data);
        for(int i=0;i<mHomeMenuIconArray.length;i++){
            MenuBean bean = new MenuBean();
            bean.setIconId(mHomeMenuIconArray[i]);
            bean.setDec(mHomeMenuTextArray[i]);
            mListData.add(bean);
        }
    }


    public void opneFragmentManage(int type){
        Intent intent = new Intent(this ,ManagerHomeFragmentActivity.class);
        intent.putExtra("type",type);
        startActivity(intent);
    }

}
