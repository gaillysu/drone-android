package com.dayton.drone.activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.GalleryListAdapter;
import com.readystatesoftware.systembartint.SystemBarTintManager;

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
    private int[] droneImagesIdArray = new int[]{R.mipmap.welcome_logo_1,R.mipmap.welcome_logo_2,
            R.mipmap.welcome_logo_3,R.mipmap.welcome_logo_4,R.mipmap.welcome_logo_5,R.mipmap.welcome_logo_6};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);//通知栏所需颜色
        }

        ButterKnife.bind(this);
        titleTextView.setText(getString(R.string.gallery_activity_title));
        listView.setAdapter(new GalleryListAdapter(this ,droneImagesIdArray));
    }

    @OnClick(R.id.content_title_back_bt)
    public void backClick() {
        finish();
    }
}
