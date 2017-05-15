package com.dayton.drone.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.map.builder.MapBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by med on 17/5/15.
 */

public class NavigationActivity extends BaseActivity {

    @Bind(R.id.map_content)
    RelativeLayout mapLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        ButterKnife.bind(this);
        new MapBuilder(mapLayout,this).build(savedInstanceState);
     }
}
