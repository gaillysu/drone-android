package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.database.entry.UserDatabaseHelper;
import com.dayton.drone.modle.User;

/**
 * Created by boy on 2016/4/15.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_sexFamale;
    private TextView tv_sexMale;
    private String mUserSex;
    private TextView tv_userBirth, tv_userHeight, tv_userWeight;
    private int viewType = 1;
    private ImageView iv_back;
    private ImageView iv_next;
    private String userSex;

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
        tv_sexFamale.setBackgroundColor(R.color.colorPrimary);
        mUserSex = tv_sexFamale.getText().toString();
        iv_back = (ImageView) findViewById(R.id.registe_back_iv);
        iv_next = (ImageView) findViewById(R.id.registe_next_iv);
    }

    private void addListener() {
        tv_sexFamale.setOnClickListener(this);
        tv_sexMale.setOnClickListener(this);
        tv_userBirth.setOnClickListener(this);
        tv_userHeight.setOnClickListener(this);
        tv_userWeight.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_sex_fmale:
                mUserSex = tv_sexFamale.getText().toString();
                tv_sexFamale.setBackgroundColor(R.color.colorPrimary);
                tv_sexMale.setBackgroundColor(android.R.color.transparent);
                break;
            case R.id.user_sex_male:
                mUserSex = tv_sexMale.getText().toString();
                tv_sexFamale.setBackgroundColor(android.R.color.transparent);
                tv_sexMale.setBackgroundColor(R.color.colorPrimary);
                break;

            //userinfo
            case R.id.user_barthday:
                viewType = 1;
                DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(UserInfoActivity.this,
                        new DatePickerPopWin.OnDatePickedListener() {
                            @Override
                            public void onDatePickCompleted(int year, int month,
                                                            int day, String dateDesc) {
                                Toast.makeText(UserInfoActivity.this, dateDesc, 0).show();
                                tv_userBirth.setText(dateDesc);
                            }
                        }).viewStyle(viewType)
                        .viewTextSize(25) // pick view text size
                        .minYear(1990) //min year in loop
                        .maxYear(2550) // max year in loop
                        .dateChose("2000-11-11") // date chose when init popwindow
                        .build();
                pickerPopWin.showPopWin(UserInfoActivity.this);
                break;
            case R.id.user_height:
                viewType = 2;
                DatePickerPopWin pickerPopWin2 = new DatePickerPopWin.Builder(UserInfoActivity.this,
                        new DatePickerPopWin.OnDatePickedListener() {
                            @Override
                            public void onDatePickCompleted(int year, int month,
                                                            int day, String dateDesc) {
                                tv_userHeight.setText(dateDesc);
                            }
                        }).viewStyle(viewType)
                        .viewTextSize(25) // pick view text size
                        .dateChose("0-173-0") // date chose when init popwindow
                        .build();
                pickerPopWin2.showPopWin(UserInfoActivity.this);
                break;
            case R.id.user_weight:
                viewType = 3;
                DatePickerPopWin pickerPopWin3 = new DatePickerPopWin.Builder(UserInfoActivity.this,
                        new DatePickerPopWin.OnDatePickedListener() {
                            @Override
                            public void onDatePickCompleted(int year, int month,
                                                            int day, String dateDesc) {
                                tv_userWeight.setText(dateDesc);
                            }
                        }).viewStyle(viewType)
                        .viewTextSize(25) // pick view text size
                        .dateChose("52-0-0") // date chose when init popwindow
                        .build();
                pickerPopWin3.showPopWin(UserInfoActivity.this);
                break;

            case R.id.registe_back_iv:
                finish();
                break;
            case R.id.registe_next_iv:
                String birthday = tv_userBirth.getText().toString();
                String height = tv_userHeight.getText().toString();
                String weight = tv_userWeight.getText().toString();
                if (!(TextUtils.isEmpty(birthday)&&TextUtils.isEmpty(height)&&TextUtils.isEmpty(weight))){

                    Intent intent = getIntent();
                    String account = intent.getStringExtra("account");
                    String password = intent.getStringExtra("password");
                    UserDatabaseHelper database = new UserDatabaseHelper(UserInfoActivity.this);
                    User us = new User(System.currentTimeMillis());
                    int h = new Integer(height.substring(0, 3));
                    int w = new Integer(weight.substring(0, weight.length() - 2));
                    us.setBirthday(birthday);
                    us.setDroneUserEmail(account);
                    us.setHeight(h);
                    us.setWeight(w);
                    if (mUserSex.equals("famale")) {
                        us.setSex(1);
                    } else {
                        us.setSex(2);
                    }
                    database.add(us);

                    startActivity(SelectDevice.class);

                } else {
                    Toast.makeText(UserInfoActivity.this,
                            R.string.user_info_about, Toast.LENGTH_SHORT).show();
                }
        }

    }
}
