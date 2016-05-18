package com.dayton.drone.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.dayton.drone.R;
import com.dayton.drone.model.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/18.
 */
public class ProfileActivity extends BaseActivity {
    private User mUser;
    @Bind(R.id.profile_activity_user_name_tv)
    TextView accountName;
    @Bind(R.id.content_title_dec_tv)
    TextView titleText;
    @Bind(R.id.profile_activity_user_fist_name)
    TextView userFistName;
    @Bind(R.id.profile_activity_user_email_account)
    TextView emailAccount;
    @Bind(R.id.profile_activity_user_height)
    TextView userHeight;
    @Bind(R.id.profile_activity_user_weight)
    TextView userWeight;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        ButterKnife.bind(this);
        titleText.setText(R.string.home_guide_profile);
        mUser =  getModel().getUser();
        accountName.setText(mUser.getLastName());
        userFistName.setText(mUser.getFirstName());
        emailAccount.setText(mUser.getUserEmail());
        userHeight.setText(mUser.getHeight()+"cm");
        userWeight.setText(mUser.getWeight()+"kg");
    }
    
    @OnClick(R.id.profile_activity_edit_user_name_ib)
    public void editUserName(){

    }

    @OnClick(R.id.content_title_back_bt)
    public void back(){
        finish();
    }
}
