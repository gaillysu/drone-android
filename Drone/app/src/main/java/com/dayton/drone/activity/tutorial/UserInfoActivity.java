package com.dayton.drone.activity.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.network.Constants;
import com.dayton.drone.network.request.CreateUserRequest;
import com.dayton.drone.network.request.model.CreateUser;
import com.dayton.drone.network.response.model.CreateUserModel;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/15.
 */
public class UserInfoActivity extends BaseActivity {

    @Bind(R.id.user_info_sex_female_tv)
    TextView tv_sexFemale;
    @Bind(R.id.user_info_sex_male_tv)
    TextView tv_sexMale;
    @Bind(R.id.user_barthday)
    TextView tv_userBirth;
    @Bind(R.id.user_height)
    TextView tv_userHeight;
    @Bind(R.id.user_weight)
    TextView tv_userWeight;

    private int gender = 1; //0:female, 1: male
    private int viewType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.user_info_sex_female_tv)
    public void selectUserSexFemale() {
        gender = 0;
        tv_sexFemale.setBackgroundColor(getResources().getColor(R.color.user_info_sex_bg));
        tv_sexMale.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    @OnClick(R.id.user_info_sex_male_tv)
    public void selectUserSexMale() {
        gender = 1;
        tv_sexFemale.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tv_sexMale.setBackgroundColor(getResources().getColor(R.color.user_info_sex_bg));
    }

    @OnClick(R.id.user_barthday)
    public void putUserBarthday() {
        viewType = 1;
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(UserInfoActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {
                        tv_userBirth.setText(dateDesc);
                    }
                }).viewStyle(viewType)
                .viewTextSize(25) // pick view text size
                .minYear(1990) //min year in loop
                .maxYear(2550) // max year in loop
                .dateChose("2000-11-11") // date chose when init popwindow
                .build();
        pickerPopWin.showPopWin(UserInfoActivity.this);
    }

    @OnClick(R.id.user_height)
    public void putUserHeight() {
        viewType = 2;
        DatePickerPopWin pickerPopWin2 = new DatePickerPopWin.Builder(UserInfoActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {
                        tv_userHeight.setText(dateDesc);
                    }
                }).viewStyle(viewType)
                .viewTextSize(25)
                .dateChose("0-173-0")
                .build();
        pickerPopWin2.showPopWin(UserInfoActivity.this);
    }


    @OnClick(R.id.user_weight)
    public void putUserWeight() {
        viewType = 3;
        DatePickerPopWin pickerPopWin3 = new DatePickerPopWin.Builder(UserInfoActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {
                        tv_userWeight.setText(dateDesc);
                    }
                }).viewStyle(viewType)
                .viewTextSize(25)
                .dateChose("52-0-0")
                .build();
        pickerPopWin3.showPopWin(UserInfoActivity.this);
    }


    @OnClick(R.id.registe_back_iv)
    public void back() {
        startActivity(RegisteActivity.class);
        finish();
    }

    @OnClick(R.id.registe_next_iv)
    public void next() {
        String birthday = tv_userBirth.getText().toString();
        String height = tv_userHeight.getText().toString();
        String weight = tv_userWeight.getText().toString();
        if (!(TextUtils.isEmpty(birthday) || TextUtils.isEmpty(height) || TextUtils.isEmpty(weight)))
        {
            Intent intent = getIntent();
            final String account = intent.getStringExtra("account");
            final String password = intent.getStringExtra("password");
            final int h = new Integer(height.substring(0, 3));
            final double w = Double.parseDouble(weight.substring(0, weight.length() - 2));
            int age = 25;
            try {
                age = new Date().getYear() -  new SimpleDateFormat("yyyy-MM-dd").parse(birthday).getYear();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            CreateUser createUser = new CreateUser();
            createUser.setEmail(account);
            createUser.setPassword(password);
            createUser.setFirst_name(account);
            createUser.setLast_name(account);
            createUser.setLength(h);
            createUser.setAge(age);

            final int finalAge = age;
            getModel().getRetrofitManager().execute(new CreateUserRequest(createUser, getModel().getRetrofitManager().getAccessToken()), new RequestListener<CreateUserModel>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    spiceException.printStackTrace();
                }

                @Override
                public void onRequestSuccess(CreateUserModel createUserModel) {
                    if(createUserModel.getStatus() == Constants.STATUS_CODE.STATUS_SUCCESS)
                    {
                        getModel().getUser().setFirstName(createUserModel.getUser().getFirst_name());
                        getModel().getUser().setLastName(createUserModel.getUser().getLast_name());
                        getModel().getUser().setAge(finalAge);
                        getModel().getUser().setHeight(h);
                        getModel().getUser().setWeight(w);
                        getModel().getUser().setGender(gender);
                        getModel().getUser().setUserEmail(createUserModel.getUser().getEmail());
                        getModel().getUser().setUserPassword(password);
                        getModel().getUser().setUserID(createUserModel.getUser().getId()+"");
                        getModel().getUser().setUserIsLogin(true);
                        getModel().getUserDatabaseHelper().update(getModel().getUser());
                    }
                }
            });

            startActivity(SelectDeviceActivity.class);
            finish();
        }
        else
        {
            Toast.makeText(UserInfoActivity.this,
                    R.string.user_info_about, Toast.LENGTH_SHORT).show();
        }
    }


}
