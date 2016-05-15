package com.dayton.drone.activity.base.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.SelectDeviceGridViewAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by boy on 2016/4/19.
 */
public class SelectDeviceActivity extends BaseActivity {
    @Bind(R.id.registe_back_iv)
   ImageButton titleBack;
    @Bind(R.id.registe_next_iv)
    ImageButton titleNext;
    @Bind(R.id.select_user_device)
    GridView mShowWatchGridView;
    private SelectDeviceGridViewAdapter mGridViewAdapter;
    private int type;

    private String[] watchNameArray;
    private int[] watchIconArray = {
            R.mipmap.drone_mens_black_strap, R.mipmap.drone_mens_tone_split_dial,
            R.mipmap.drone_mens_split_dial,R.mipmap.drone_ladies_stainless_steel,
            R.mipmap.drone_white_strap_rosetone,R.mipmap.drone_ladies_crystal_bezel,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_user_select_watch);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        type = intent.getIntExtra("type",-1);
        initView();
    }

    private void initView() {
        titleNext.setVisibility(View.GONE);
        watchNameArray = this.getResources().getStringArray(R.array.user_select_dec_arr);
        mGridViewAdapter = new SelectDeviceGridViewAdapter(watchIconArray,watchNameArray,this);
        mShowWatchGridView.setAdapter(mGridViewAdapter);
        mShowWatchGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int iconId = watchIconArray[position];
                String watchName =watchNameArray[position];
                Intent intent =new Intent(SelectDeviceActivity.this,ShowWatchActivity.class);
                intent.putExtra("watchIconId",iconId);
                intent.putExtra("selectWatchName",watchName);
                startActivity(intent);
                finish();
            }
        });

        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type == 2>>4){
                    startActivity(LoginActivity.class);
                }else{
                    startActivity(UserInfoActivity.class);
                }
                finish();
            }
        });
    }
}
