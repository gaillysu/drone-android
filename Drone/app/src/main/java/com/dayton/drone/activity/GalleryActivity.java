package com.dayton.drone.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.GalleryListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/10.
 */
public class GalleryActivity extends BaseActivity {

    @Bind(R.id.content_title_dec_tv)
    TextView titleTextView;
    @Bind(R.id.activity_gallery_list)
    ListView listView;
    private int[] droneImagesIdArray = new int[]{R.drawable.welcome_drone_1,R.drawable.welcome_drone_2,R.drawable.welcome_drone_3,R.drawable.welcome_drone_4,R.drawable.welcome_drone_5,R.drawable.welcome_drone_6};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_layout);
        ButterKnife.bind(this);
        titleTextView.setText(getString(R.string.gallery_activity_title));
        listView.setAdapter(new GalleryListAdapter(this ,droneImagesIdArray));
    }

    @OnClick(R.id.content_title_back_bt)
    public void backClick() {
        finish();
    }
}
