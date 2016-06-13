package com.dayton.drone.activity.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.dayton.drone.R;
import com.dayton.drone.activity.AddWatchActivity;
import com.dayton.drone.activity.HomeActivity;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.SelectDeviceGridViewAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/19.
 */
public class SelectDeviceActivity extends BaseActivity {
    @Bind(R.id.register_back_iv)
    ImageButton titleBack;
    @Bind(R.id.register_next_iv)
    ImageButton titleNext;
    @Bind(R.id.select_user_device)
    GridView showWatchGridView;
    private int type;
    private int loginType = 2 >> 4;
    private int addWatchType = 5<<3;

    private String[] watchNameArray;
    private int[] droneImagesIdArray = new int[]{R.mipmap.welcome_logo_1, R.mipmap.welcome_logo_2,
            R.mipmap.welcome_logo_3, R.mipmap.welcome_logo_4, R.mipmap.welcome_logo_5, R.mipmap.welcome_logo_6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_user_select_watch);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        initView();

    }

    private void initView() {

        titleNext.setVisibility(View.GONE);
        watchNameArray = this.getResources().getStringArray(R.array.user_select_dec_arr);
        SelectDeviceGridViewAdapter gridViewAdapter = new SelectDeviceGridViewAdapter(droneImagesIdArray, watchNameArray, this);
        showWatchGridView.setAdapter(gridViewAdapter);

        showWatchGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int iconId = droneImagesIdArray[position];
                String watchName = watchNameArray[position];
                Intent intent = new Intent(SelectDeviceActivity.this, ShowWatchActivity.class);
                intent.putExtra("watchIconId", iconId);
                intent.putExtra("selectWatchName", watchName);
                startActivity(intent);
                finish();
            }
        });

        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == loginType) {
                    startActivity(LoginActivity.class);
                } else if (type == addWatchType) {
                    startActivity(AddWatchActivity.class);
                } else {
                    startActivity(UserInfoActivity.class);
                }
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (type == loginType) {
                startActivity(LoginActivity.class);
            } else if (type == addWatchType) {
                startActivity(AddWatchActivity.class);
            } else {
                startActivity(UserInfoActivity.class);
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.select_activity_open_home_activity)
    public void openHomeActivity(){
        startActivity(HomeActivity.class);
        finish();
    }
}
