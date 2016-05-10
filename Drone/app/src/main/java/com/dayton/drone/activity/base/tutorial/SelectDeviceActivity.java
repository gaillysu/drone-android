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

/**
 * Created by boy on 2016/4/19.
 */
public class SelectDeviceActivity extends BaseActivity {

    private ImageButton mIvBack;
    private ImageButton mIvNext;
    private GridView mShowWatchGridView;
    private SelectDeviceGridViewAdapter mGridViewAdapter;
    private int type;

    private String[] watchNameArray;
    private int[] watchIconArray = {
            R.mipmap.drone_mens_black_strap, R.mipmap.drone_mens_tone_split_dial, R.mipmap.drone_white_strap_rosetone,R.mipmap.drone_mens_split_dial,
            R.mipmap.drone_ladies_crystal_bezel, R.mipmap.drone_ladies_stainless_steel,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_user_select_watch);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type",-1);
        mIvBack = (ImageButton) findViewById(R.id.registe_back_iv);
        mIvNext = (ImageButton) findViewById(R.id.registe_next_iv);
        mShowWatchGridView = (GridView) findViewById(R.id.select_user_device);
        mIvNext.setVisibility(View.GONE);
        if(type == 2>>4){
            mIvBack.setVisibility(View.GONE);
        }
        watchNameArray = this.getResources().getStringArray(R.array.user_select_dec_arr);
        mGridViewAdapter = new SelectDeviceGridViewAdapter(watchIconArray,watchNameArray);
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

        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
