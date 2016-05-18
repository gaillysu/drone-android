package com.dayton.drone.activity.base.tutorial;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dayton.drone.R;
import com.dayton.drone.activity.HomeActivity;
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
    @Bind(R.id.tutorial_activity_select_point)
    ImageView currentPageFlag;

    private List<ImageView> mVpList;
    private int[] vp_data = new int[]{R.mipmap.drone_mens_black_strap,
            R.mipmap.drone_mens_tone_split_dial, R.mipmap.drone_white_strap_rosetone,
            R.mipmap.drone_mens_split_dial};
    private int mPointSpace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if one user has got logged in, directly entry main menu screen
        if (getModel().getUser().isUserIsLogin()) {
            startActivity(HomeActivity.class);
            finish();
            return;
        }
        setContentView(R.layout.activtiy_login_tutorial);
        ButterKnife.bind(this);
        initDate();
        currentPageFlag.getViewTreeObserver().addOnGlobalLayoutListener
                (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mPointSpace=  pointGroup.getChildAt(1).getLeft() - pointGroup.getChildAt(0).getLeft();
            }
        });
    }

    private void initDate() {
        mVpList = new ArrayList<>(vp_data.length);
        for (int iv : vp_data) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(iv);
            mVpList.add(imageView);
        }

        vp_loginPage.setAdapter(new TutorialViewpagerAdapter(this, mVpList));

        for (int x = 0; x < vp_data.length; x++) {
            ImageView pointImageView = new ImageView(this);
                pointImageView.setBackgroundResource(R.drawable.uncheck_point_shape);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (x != 0) {
                lp.leftMargin = 15;
            }
            pointImageView.setLayoutParams(lp);
            pointGroup.addView(pointImageView);
        }

        vp_loginPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int marginLeft = (int) (mPointSpace*positionOffset+position*mPointSpace+0.5f);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) currentPageFlag.getLayoutParams();
                layoutParams.leftMargin = marginLeft;
                currentPageFlag.setLayoutParams(layoutParams);

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.login_bt)
    public void openLoginActivity() {
        startActivity(LoginActivity.class);
        finish();
    }

    @OnClick(R.id.registe_bt)
    public void openRegisteActivity() {
        startActivity(RegisteActivity.class);
        finish();
    }
}

