package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.activity.base.tutorial.ShowWatchActivity;

/**
 * Created by boy on 2016/4/19.
 */
public class SelectDevice extends BaseActivity {

    private ImageView iv_back;
    private ImageView iv_next;
    private GridView gv_selectDevice;
    private SelectAdapter mAdapter;

    private String[] mDec;
    private int[] arr = new int[]{
            R.mipmap.icon_04, R.mipmap.icon_04,
            R.mipmap.icon_04, R.mipmap.icon_04,
            R.mipmap.icon_04, R.mipmap.icon_04,
            R.mipmap.icon_04, R.mipmap.icon_04,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_user_select_watch);
        initView();
        initEvent();
    }


    private void initView() {
        iv_back = (ImageView) findViewById(R.id.registe_back_iv);
        iv_next = (ImageView) findViewById(R.id.registe_next_iv);
        gv_selectDevice = (GridView) findViewById(R.id.select_user_device);
        iv_next.setVisibility(View.GONE);
        mDec = this.getResources().getStringArray(R.array.user_select_dec_arr);
    }

    private class SelectAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return arr.length;
        }

        @Override
        public Object getItem(int position) {
            return arr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HolderView holder = null;
            if (convertView == null) {
                convertView = View.inflate(SelectDevice.this, R.layout.item_select_watch, null);
                holder = new HolderView();
                holder.iv_drone = (ImageView) convertView.findViewById(R.id.select_watch_icon);
                holder.tv_name = (TextView) convertView.findViewById(R.id.drone_name);
                convertView.setTag(holder);
            } else {
                holder = (HolderView) convertView.getTag();
            }
            if (mDec.length > 0 && arr.length > 0) {
                holder.iv_drone.setImageResource(arr[position]);
                holder.tv_name.setText(mDec[position]);
            }
            return convertView;
        }
    }

    static class HolderView {
        ImageView iv_drone;
        TextView tv_name;
    }

    private void initEvent() {
        mAdapter = new SelectAdapter();
        gv_selectDevice.setAdapter(mAdapter);
        gv_selectDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectDevice.this, ShowWatchActivity.class);
                intent.putExtra("image_id", arr[position]);
                intent.putExtra("watch_name", mDec[position]);
                startActivity(intent);

            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}