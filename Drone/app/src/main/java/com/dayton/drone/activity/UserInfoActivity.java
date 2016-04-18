package com.dayton.drone.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;

/**
 * Created by boy on 2016/4/15.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_sexFamale;
    private TextView tv_sexMale;
    private String mUserSex;
    private TextView tv_userBirth,tv_userHeight,tv_userWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        addListener();
    }

    private void initView() {
        tv_sexFamale = (TextView) findViewById(R.id.user_sex_fmale);
        tv_sexMale = (TextView) findViewById(R.id.user_sex_male);
        tv_userBirth = (TextView) findViewById(R.id.user_barthday);
        tv_userHeight = (TextView) findViewById(R.id.user_height);
        tv_userWeight = (TextView) findViewById(R.id.user_weight);
    }

    private void addListener() {
        tv_sexFamale.setOnClickListener(this);
        tv_sexMale.setOnClickListener(this);
        tv_userBirth.setOnClickListener(this);
        tv_userHeight.setOnClickListener(this);
        tv_userWeight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.user_sex_fmale:
                mUserSex  = tv_sexFamale.getText().toString();
                tv_sexFamale.setBackgroundColor(R.color.colorPrimary);
                tv_sexMale.setBackgroundColor(android.R.color.transparent);
               break;
            case R.id.user_sex_male:
                mUserSex = tv_sexMale.getText().toString();
                tv_sexFamale.setBackgroundColor(android.R.color.transparent);
                tv_sexMale.setBackgroundColor(R.color.colorPrimary);
                break;
            case R.id.user_barthday:
                //Todo
                break;
            case R.id.user_height:
                //TODO
                break;
            case R.id.user_weight:
                //TODO
                break;

        }
    }
}
