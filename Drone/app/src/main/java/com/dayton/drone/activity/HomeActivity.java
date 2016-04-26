package com.dayton.drone.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.fragment.AnalYisisFragment;
import com.dayton.drone.fragment.HomeContentFragment;
import com.dayton.drone.fragment.ProfileFragment;
import com.dayton.drone.fragment.WatchSettingFragment;
import com.dayton.drone.fragment.WorldClockFragment;

/**
 * Created by boy on 2016/4/21.
 */
public class HomeActivity extends Activity {


    private TextView mMonthTv;
    private FrameLayout mContentFragment;
    private RadioGroup mRadioGroup;
    private Bundle savedInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        savedInstance = savedInstanceState;
        initView();
    }

    private void initView() {

        mMonthTv = (TextView) findViewById(R.id.table_date_tv);
        mContentFragment = (FrameLayout) findViewById(R.id.home_middle_content);
        mRadioGroup = (RadioGroup) findViewById(R.id.home_guide_group);
        mRadioGroup.setOnCheckedChangeListener(new MyRadioGroupCheckeedChangeListeren());
        getFragmentManager().beginTransaction()
                .replace(R.id.home_middle_content, new HomeContentFragment()).commit();

    }

    private class MyRadioGroupCheckeedChangeListeren implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId) {
                case R.id.home_guide_noe:
                    if (savedInstance == null) {
                        getFragmentManager().beginTransaction().
                                replace(R.id.home_middle_content, new WorldClockFragment()).commit();
                    }
                    break;
                case R.id.home_guide_two:
                    if (savedInstance == null) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.home_middle_content, new AnalYisisFragment()).commit();
                    }
                    break;
                case R.id.home_guide_three:
                    if (savedInstance == null) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.home_middle_content, new HomeContentFragment()).commit();
                    }
                    break;
                case R.id.home_guide_four:
                    if (savedInstance == null) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.home_middle_content, new WatchSettingFragment()).commit();
                    }
                    break;
                case R.id.home_guide_five:
                    if (savedInstance == null) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.home_middle_content, new ProfileFragment()).commit();
                    }
                    break;
            }
        }


    }
}
