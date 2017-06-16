package com.dayton.drone.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.GalleryListAdapter;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/10.
 */
public class GalleryActivity extends BaseActivity {

    @Bind(R.id.my_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.activity_gallery_list)
    ListView listView;
    private int[] droneImagesIdArray = new int[]{R.mipmap.welcome_logo_1,R.mipmap.welcome_logo_2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }

        ButterKnife.bind(this);
        initToolbar();
        listView.setAdapter(new GalleryListAdapter(this ,droneImagesIdArray));
    }



    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        TextView titleTv = (TextView) mToolbar.findViewById(R.id.toolbar_title_tv);
        titleTv.setText(getString(R.string.gallery_activity_title));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
