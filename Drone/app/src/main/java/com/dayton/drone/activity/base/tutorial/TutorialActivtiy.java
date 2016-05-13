package com.dayton.drone.activity.base.tutorial;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.adapter.TutorialViewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/13.
 */
public class TutorialActivtiy extends BaseActivity {

    @Bind(R.id.activity_login_vp)
    ViewPager vp_loginPage;
    @Bind(R.id.view_pager_point_group)
    LinearLayout pointGroup;

    private List<ImageView> mVpList;
    private int[] vp_data = new int[]{R.mipmap.drone_mens_black_strap,
            R.mipmap.drone_mens_tone_split_dial, R.mipmap.drone_white_strap_rosetone,
            R.mipmap.drone_mens_split_dial};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_login_tutorial);
        ButterKnife.bind(this);
        initDate();
    }

    private void initDate() {
        mVpList = new ArrayList<>(vp_data.length);
        for (int iv : vp_data) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(iv);
            mVpList.add(imageView);
        }

        for(int x= 0;x<vp_data.length;x++){
            ImageView pointImageView = new ImageView(this);
            if(x == vp_loginPage.getCurrentItem()){
                pointImageView.setImageResource(R.drawable.select_poine);
            }else{
                pointImageView.setBackgroundResource(R.drawable.uncheck_point_shape);
            }
            pointGroup.addView(pointImageView);
        }

        vp_loginPage.setAdapter(new TutorialViewpagerAdapter(this ,mVpList));


    }

    @OnClick(R.id.login_bt)
    public void openLoginActivity() {
        startActivity(LoginActivtiy.class);
        finish();
    }

    @OnClick(R.id.registe_bt)
    public void openRegisteActivity() {
        startActivity(RegisteActivity.class);
        finish();
    }
}

