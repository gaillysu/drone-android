package com.dayton.drone.activity.base.tutorial;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.database.entry.UserDatabaseHelper;
import com.dayton.drone.modle.User;
import com.dayton.drone.network.Constants;
import com.dayton.drone.network.request.CreateUserRequest;
import com.dayton.drone.network.request.model.CreateUser;
import com.dayton.drone.network.response.model.CreateUserModel;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/15.
 */
public class UserInfoActivity extends BaseActivity {

    @Bind(R.id.user_info_sex_famale_tv)
    TextView tv_sexFamale;
    @Bind(R.id.user_info_sex_male_tv)
    TextView tv_sexMale;
    @Bind(R.id.user_barthday)
    TextView tv_userBirth;
    @Bind(R.id.user_height)
    TextView tv_userHeight;
    @Bind(R.id.user_weight)
    TextView tv_userWeight;
    @Bind(R.id.registe_back_iv)
    ImageButton ivBack;
    @Bind(R.id.registe_next_iv)
    ImageButton ivNext;
    private String userSex;
    private int viewType = 1;
    private String mUserSex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        tv_sexFamale.setBackgroundColor(R.color.userinfo_sex_bg);
        mUserSex = tv_sexFamale.getText().toString();
    }

    @OnClick(R.id.user_info_sex_famale_tv)
    public void selectUserSexFemale() {
        mUserSex = tv_sexFamale.getText().toString();
        tv_sexFamale.setBackgroundColor(R.color.userinfo_sex_bg);
        tv_sexMale.setBackgroundColor(android.R.color.transparent);
    }

    @OnClick(R.id.user_info_sex_male_tv)
    public void selectUserSexMale() {
        mUserSex = tv_sexMale.getText().toString();
        tv_sexFamale.setBackgroundColor(android.R.color.transparent);
        tv_sexMale.setBackgroundColor(R.color.userinfo_sex_bg);
    }

    @OnClick(R.id.user_barthday)
    public void putUserBarthday() {

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
                .viewTextSize(25) // pick view text size
                .dateChose("0-173-0") // date chose when init popwindow
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
                .viewTextSize(25) // pick view text size
                .dateChose("52-0-0") // date chose when init popwindow
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
        if (!(TextUtils.isEmpty(birthday) && TextUtils.isEmpty(height) && TextUtils.isEmpty(weight)))

        {

            Intent intent = getIntent();
            String account = intent.getStringExtra("account");
            String password = intent.getStringExtra("password");
            int h = new Integer(height.substring(0, 3));
            double w = Double.parseDouble(weight.substring(0, weight.length() - 2));
            us.setUserEmail(account);
            us.setHeight(h);
            us.setWeight(w);
            if (mUserSex.equals("famale")) {
                us.setGender(0);
            } else {
                us.setGender(1);
            }
            database.add(us);

            CreateUser createUser = new CreateUser();
            createUser.setEmail(account);
            createUser.setPassword(password);
            createUser.setFirst_name(account);
            createUser.setLast_name(account);
            createUser.setLength(h);
            //createUser.setAge();

            getModel().getRetrofitManager().execute(new CreateUserRequest(createUser, getModel().getRetrofitManager().getAccessToken()), new RequestListener<CreateUserModel>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    spiceException.printStackTrace();
                }

                @Override
                public void onRequestSuccess(CreateUserModel createUserModel) {
                    if(createUserModel.getStatus() == Constants.STATUS_CODE.STATUS_SUCCESS)
                    {
                        getModel().getUser().setUserEmail(createUserModel.getUser().getEmail());
                        getModel().getUser().setUserID(createUserModel.getUser().getId()+"");
                        getModel().getUser().setUserIsLogin(true);
                        //TODO save to database
                    }
                }
            });

            IntentFilter filter = new IntentFilter();
            filter.addAction("CLOSE_ACTIVITY");
            registerReceiver(broadcast,filter);

            startActivity(SelectDeviceActivity.class);
             finish();
        } else

        {
            Toast.makeText(UserInfoActivity.this,
                    R.string.user_info_about, Toast.LENGTH_SHORT).show();
        }
    }


}
