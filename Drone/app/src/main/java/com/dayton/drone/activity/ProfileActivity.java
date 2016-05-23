package com.dayton.drone.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.database.entry.UserDatabaseHelper;
import com.dayton.drone.model.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/5/18.
 */
public class ProfileActivity extends BaseActivity {
    private User mUser;
    @Bind(R.id.profile_activity_user_name_tv)
    EditText accountName;
    @Bind(R.id.content_title_dec_tv)
    TextView titleText;
    @Bind(R.id.profile_activity_user_fist_name)
    EditText userFirstName;
    @Bind(R.id.profile_activity_user_email_account)
    EditText emailAccount;
    @Bind(R.id.profile_activity_user_height)
    EditText userHeight;
    @Bind(R.id.profile_activity_user_weight)
    EditText userWeight;
    @Bind(R.id.profile_activity_step_goal_et)
    EditText stepGoal;
    @Bind(R.id.home_title_right_save_bt)
    Button saveButton;
    @Bind(R.id.content_title_back_bt)
    Button canael;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        ButterKnife.bind(this);
        saveButton.setVisibility(View.VISIBLE);
        canael.setText(getString(R.string.home_title_back_text));
        user = getModel().getUser();
        titleText.setText(getString(R.string.home_guide_profile));
        mUser =  getModel().getUser();
        accountName.setText(mUser.getLastName() != null? mUser.getLastName():getResources().getString(R.string.profile_edit_prompt));
        userFirstName.setText(mUser.getFirstName()!=null?mUser.getFirstName():getResources().getString(R.string.profile_edit_prompt));
        emailAccount.setText(mUser.getUserEmail() !=null?mUser.getUserEmail():getResources().getString(R.string.profile_edit_prompt));
        int userHeightValue = mUser.getHeight();
        if(userHeightValue>50){
            userHeight.setText(userHeightValue+getResources().getString(R.string.profile_user_height_unit));
        }else{
            userHeight.setText(getResources().getString(R.string.profile_edit_prompt));
        }
        double userWeightValue = mUser.getWeight();
        if(userHeightValue>15){
            userWeight.setText(userWeightValue+getResources().getString(R.string.profile_user_weight_unit));
        }else{
            userWeight.setText(getResources().getString(R.string.profile_edit_prompt));
        }
    }

    @OnClick(R.id.profile_activity_edit_user_name_ib)
    public void editUserName(){

    }

    @OnClick(R.id.content_title_back_bt)
    public void back(){
        finish();
    }

    @OnClick(R.id.profile_activity_edit_user_name_ib)
    public void editUserLastNameOnClick(){
        accountName.requestFocus();
        accountName.setText("");
        accountName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String lastName = accountName.getText().toString();
                    if(!TextUtils.isEmpty(lastName)){
                        user.setLastName(lastName);
                        accountName.setText(lastName);
                    }else{
                        accountName.setText(getResources().getString(R.string.profile_edit_prompt));
                        Toast.makeText(ProfileActivity.this,R.string.profile_prompt_user_edit,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_fist_name_ib)
    public void editUserFirstName(){
        userFirstName.requestFocus();
        userFirstName.setText("");
        userFirstName.isFocusable();
        userFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String firstName =userFirstName.getText().toString();
                    if(!TextUtils.isEmpty(firstName)){
                        user.setFirstName(firstName);
                        userFirstName.setText(firstName);
                    }else{
                        userFirstName.setText(R.string.profile_edit_prompt);
                        Toast.makeText(ProfileActivity.this,R.string.profile_prompt_user_edit,Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_email_ib)
    public void editUserEmailClick(){
        emailAccount.requestFocus();
        emailAccount.setText("");
        emailAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String email = emailAccount.getText().toString();
                    if(TextUtils.isEmpty(email)){
                      boolean flag = checkEmail(email);
                        if(flag){
                            user.setUserEmail(email);
                            emailAccount.setText(email);
                        }else{
                            emailAccount.setText(user.getUserEmail());
                            Toast.makeText(ProfileActivity.this,
                                    getString(R.string.profile_email_format_error_prompt),Toast.LENGTH_SHORT).show();

                        }
                    }else{
                        emailAccount.setText(user.getUserEmail());
                    }
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_user_height)
    public void editUserHright(){
        userHeight.requestFocus();
        userHeight.setText("");
        userHeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String height = userHeight.getText().toString();
                    if(!TextUtils.isEmpty(height)){
                        int userH = new Integer(height).intValue();
                        if(userH<300&&userH>50){
                            user.setHeight(userH);
                            userHeight.setText(userH+getResources().getString(R.string.profile_user_height_unit));
                        }else{
                            userHeight.setText(user.getHeight()+getResources().getString(R.string.profile_user_height_unit));
                            Toast.makeText(ProfileActivity.this,getString(R.string.profile_user_height_error),Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        userHeight.setText(user.getHeight()+getResources().getString(R.string.profile_user_height_unit));
                    }
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_user_weight)
    public void editUserWeight(){
        userWeight.requestFocus();
        userWeight.setText("");
        userWeight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String weightInput = userWeight.getText().toString();
                    if(!TextUtils.isEmpty(weightInput)){
                        double weight = Double.parseDouble(weightInput);
                        if((weight>0.0)&&(weight<300.0)){
                            user.setWeight(weight);
                            userWeight.setText(weight+getResources().getString(R.string.profile_user_weight_unit));
                        }else{
                            userHeight.setText(user.getWeight()+getResources().getString(R.string.profile_user_weight_unit));
                            Toast.makeText(ProfileActivity.this,getString(R.string.profile_user_height_error),Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        userHeight.setText(user.getWeight()+getResources().getString(R.string.profile_user_weight_unit));
                    }
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_goal_ib)
    public void editGoalClick(){
        stepGoal.requestFocus();
        stepGoal.setText("");
        stepGoal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String goal = stepGoal.getText().toString();
                    if(!TextUtils.isEmpty(goal)){
                        int goalStep = new Integer(goal).intValue();
                        if(goalStep>0&&goalStep<1000000){
                            //TODO
                        }else{
                            stepGoal.setText("10000");
                            Toast.makeText(ProfileActivity.this,getString
                                    (R.string.profile_user_height_error),Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        //TODO
                    }
                }
            }
        });
    }

    @OnClick(R.id.home_title_right_save_bt)
    public void editOver(){
        UserDatabaseHelper helper = getModel().getUserDatabaseHelper();
        helper.update(user);
    }

    public boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$/";

            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }
}
