package com.dayton.drone.activity.tutorial;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.network.Constants;
import com.dayton.drone.network.request.CreateUserRequest;
import com.dayton.drone.network.request.model.CreateUser;
import com.dayton.drone.network.response.model.CreateUserModel;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    @Bind(R.id.user_birthday)
    TextView tv_userBirth;
    @Bind(R.id.user_height)
    TextView tv_userHeight;
    @Bind(R.id.user_weight)
    TextView tv_userWeight;
    @Bind(R.id.user_info_activity_first_name_ed)
    EditText editFirstName;
    @Bind(R.id.user_info_activity_last_name_ed)
    EditText editLastName;


    private int gender = 1; //0:female, 1: male
    private int viewType = 1;
    private String userBirthdayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_gender_select_text_color);
        }
        ButterKnife.bind(this);
    }

    @OnClick(R.id.user_info_sex_female_tv)
    public void selectUserSexFemale() {
        gender = 0;
        tv_sexFemale.setBackgroundColor(getResources().getColor(R.color.user_info_sex_bg));
        tv_sexMale.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tv_sexFemale.setTextColor(getResources().getColor(R.color.user_info_gender_select_text_color));
        tv_sexMale.setTextColor(getResources().getColor(R.color.user_info_gender_default_text_color));
    }

    @OnClick(R.id.user_info_sex_male_tv)
    public void selectUserSexMale() {
        gender = 1;
        tv_sexFemale.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        tv_sexMale.setBackgroundColor(getResources().getColor(R.color.user_info_sex_bg));
        tv_sexFemale.setTextColor(getResources().getColor(R.color.user_info_gender_default_text_color));
        tv_sexMale.setTextColor(getResources().getColor(R.color.user_info_gender_select_text_color));
    }

    @OnClick(R.id.user_birthday)
    public void putUserBarthday() {
        viewType = 1;
        final Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formatDate = format.format(date);
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(UserInfoActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {
                        try {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date userSelectDate = dateFormat.parse(dateDesc);
                            tv_userBirth.setText(new SimpleDateFormat("MMM").format(userSelectDate) + "-" + day + "-" + year);
                            userBirthdayDate = dateDesc;
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }).viewStyle(viewType)
                .viewTextSize(25) // pick view text size
                .minYear(Integer.valueOf(formatDate.split("-")[0]) - 100) //min year in loop
                .maxYear(Integer.valueOf(formatDate.split("-")[0])) // max year in loop
                .dateChose((Integer.valueOf(formatDate.split("-")[0]) - 30)
                        + "-" + formatDate.split("-")[1] + "-" + formatDate.split("-")[2]) // date chose when init popwindow
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
                .dateChose("170")
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
                .dateChose("60")
                .build();
        pickerPopWin3.showPopWin(UserInfoActivity.this);
    }

    @OnClick(R.id.register_back_iv)
    public void back() {
        startActivity(RegisterActivity.class);
        finish();
    }

    @OnClick(R.id.register_next_iv)
    public void next() {
        final String birthday = tv_userBirth.getText().toString() != null ? userBirthdayDate : null;
        String height = tv_userHeight.getText().toString();
        String weight = tv_userWeight.getText().toString();
        String firstName = editFirstName.getText().toString();
        String lastName = editLastName.getText().toString();
        if (!(TextUtils.isEmpty(birthday) || TextUtils.isEmpty(height) || TextUtils.
                isEmpty(weight) || TextUtils.isEmpty(firstName))) {

            Intent intent = getIntent();
            final String account = intent.getStringExtra("account");
            final String password = intent.getStringExtra("password");
            final int h = Integer.valueOf(height.substring(0, 3));
            final double w = Double.parseDouble(weight.substring(0, weight.length() - 2));

            CreateUser createUser = new CreateUser();
            createUser.setEmail(account);
            createUser.setPassword(password);
            createUser.setLength(h);
            createUser.setBirthday(birthday);
            createUser.setFirst_name(firstName);
            if (lastName != null) {
                createUser.setLast_name(lastName);
            } else {
                createUser.setLast_name("");
            }
            createUser.setWeight((float) w);
            createUser.setSex(gender);
            getModel().getRetrofitManager().execute(new CreateUserRequest(createUser, getModel().getRetrofitManager().getAccessToken()), new RequestListener<CreateUserModel>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    spiceException.printStackTrace();
                }

                @Override
                public void onRequestSuccess(CreateUserModel createUserModel) {
                    if (createUserModel.getStatus() == Constants.STATUS_CODE.STATUS_SUCCESS) {
                        getModel().getUser().setFirstName(createUserModel.getUser().getFirst_name());
                        getModel().getUser().setLastName(createUserModel.getUser().getLast_name());
                        getModel().getUser().setBirthday(birthday);
                        getModel().getUser().setHeight(h);
                        getModel().getUser().setWeight(w);
                        getModel().getUser().setGender(gender);
                        getModel().getUser().setUserEmail(createUserModel.getUser().getEmail());
                        getModel().getUser().setUserPassword(password);
                        getModel().getUser().setUserID(createUserModel.getUser().getId() + "");
                        getModel().getUser().setUserIsLogin(true);
                        getModel().getUserDatabaseHelper().update(getModel().getUser());
                    }
                }
            });

            startActivity(SelectDeviceActivity.class);
            finish();
        } else {
            if (firstName.isEmpty()) {
                editFirstName.setError(getString(R.string.user_info_first_name_error));
            } else {
                editFirstName.setError(null);
            }
            if (birthday.isEmpty()) {
                tv_userBirth.setError(getString(R.string.user_info_birthday_error));
            } else {
                tv_userBirth.setError(null);
            }
            if (height.isEmpty()) {
                tv_userHeight.setError(getString(R.string.user_info_height_error));
            } else {
                tv_userHeight.setError(null);
            }

            if (weight.isEmpty()) {
                tv_userWeight.setError(getString(R.string.user_info_weight_error));
            } else {
                tv_userWeight.setError(null);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(RegisterActivity.class);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}