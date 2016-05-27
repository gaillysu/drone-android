package com.dayton.drone.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.database.entry.UserDatabaseHelper;
import com.dayton.drone.model.User;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.SpUtils;

import java.util.Date;
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
    TextView userHeight;
    @Bind(R.id.profile_activity_user_weight)
    TextView userWeight;
    @Bind(R.id.profile_activity_step_goal_et)
    EditText stepGoal;
    @Bind(R.id.home_title_right_save_bt)
    Button saveButton;
    @Bind(R.id.content_title_back_bt)
    Button cancel;
    @Bind(R.id.profile_activity_log_out_bt)
    Button logOut;
    @Bind(R.id.profile_save_no_watch_connected_show)
    RelativeLayout noWatchShow;
    private int userStepGoal;
    private int viewType = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        ButterKnife.bind(this);
        saveButton.setVisibility(View.VISIBLE);
        userStepGoal = SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000);
        cancel.setText(getString(R.string.home_title_back_text));
        stepGoal.setText(userStepGoal + "");
        titleText.setText(getString(R.string.home_guide_profile));
        mUser = getModel().getUser();
        accountName.setText(mUser.getLastName() != null ? mUser.getLastName() : getResources().getString(R.string.profile_edit_prompt));
        userFirstName.setText(mUser.getFirstName() != null ? mUser.getFirstName() : getResources().getString(R.string.profile_edit_prompt));
        emailAccount.setText(mUser.getUserEmail() != null ? mUser.getUserEmail() : getResources().getString(R.string.profile_edit_prompt));

        int userHeightValue = mUser.getHeight();
        if (userHeightValue > 50 && userHeightValue < 300) {
            userHeight.setText(userHeightValue + getResources().getString(R.string.profile_user_height_unit));
        } else {
            userHeight.setText(getResources().getString(R.string.profile_edit_prompt));
        }
        double userWeightValue = mUser.getWeight();
        if (userWeightValue > 15 && userWeightValue < 500) {
            userWeight.setText(userWeightValue + getResources().getString(R.string.profile_user_weight_unit));
        } else {
            userWeight.setText(getResources().getString(R.string.profile_edit_prompt));
        }
    }

    @OnClick(R.id.content_title_back_bt)
    public void back() {
        finish();
    }

    @OnClick(R.id.profile_activity_edit_user_name_ib)
    public void editUserLastNameOnClick() {
        accountName.requestFocus();
        accountName.setText("");
        accountName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String lastName = accountName.getText().toString();
                    if (!TextUtils.isEmpty(lastName)) {

                        mUser.setLastName(lastName);
                        accountName.setText(lastName);
                    } else {
                        if (mUser.getLastName() == null) {
                            accountName.setText(getResources().getString(R.string.profile_edit_prompt));
                            Toast.makeText(ProfileActivity.this, R.string.profile_prompt_user_edit, Toast.LENGTH_SHORT).show();
                        } else {
                            accountName.setText(mUser.getLastName());
                        }

                    }
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_fist_name_ib)
    public void editUserFirstName() {
        userFirstName.requestFocus();
        userFirstName.setText("");
        userFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (!isFocus) {
                    String firstName = userFirstName.getText().toString();
                    if (!TextUtils.isEmpty(firstName)) {
                        mUser.setFirstName(firstName);
                        userFirstName.setText(firstName);
                    } else {
                        if (mUser.getFirstName() == null) {
                            userFirstName.setText(R.string.profile_edit_prompt);
                            Toast.makeText(ProfileActivity.this, R.string.profile_prompt_user_edit, Toast.LENGTH_SHORT).show();
                        } else {
                            userFirstName.setText(mUser.getFirstName());
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_email_ib)
    public void editUserEmailClick() {
        emailAccount.requestFocus();
        emailAccount.setText("");
        emailAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String email = emailAccount.getText().toString();
                    if (!TextUtils.isEmpty(email)) {
                        boolean flag = checkEmail(email);
                        if (!flag) {
                            mUser.setUserEmail(email);
                            emailAccount.setText(email);
                        } else {
                            if (mUser.getUserEmail() == null) {
                                emailAccount.setText(mUser.getUserEmail());
                                Toast.makeText(ProfileActivity.this,
                                        getString(R.string.profile_email_format_error_prompt), Toast.LENGTH_SHORT).show();
                            } else {
                                emailAccount.setText(mUser.getUserEmail());
                            }
                        }
                    } else {
                        emailAccount.setText(mUser.getUserEmail());
                    }
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_user_height)
    public void editUserHeight() {
        userHeight.setText("");
        viewType = 2;
        DatePickerPopWin pickerPopWin2 = new DatePickerPopWin.Builder(ProfileActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {
                        userHeight.setText(dateDesc + getResources().getString(R.string.profile_user_height_unit));
                    }
                }).viewStyle(viewType)
                .viewTextSize(25)
                .dateChose("0-173-0")
                .build();
        pickerPopWin2.showPopWin(ProfileActivity.this);
        pickerPopWin2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
               String height = userHeight.getText().toString();
                if(TextUtils.isEmpty(height)){
                 userHeight.setText(mUser.getHeight()+getResources().getString(R.string.profile_user_height_unit));
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_user_weight)
    public void editUserWeight() {
        userWeight.setText("");
        viewType = 3;
        DatePickerPopWin pickerPopWin3 = new DatePickerPopWin.Builder(ProfileActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {
                        userWeight.setText(dateDesc + getResources().getString(R.string.profile_user_weight_unit));
                    }
                }).viewStyle(viewType)
                .viewTextSize(25)
                .dateChose("52-0-0")
                .build();
        pickerPopWin3.showPopWin(ProfileActivity.this);
        pickerPopWin3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                String weight = userWeight.getText().toString();
                if(TextUtils.isEmpty(weight)){
                    userWeight.setText(mUser.getWeight()+getResources().getString(R.string.profile_user_weight_unit));
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_goal_ib)
    public void editGoalClick() {
        stepGoal.requestFocus();
        stepGoal.setText("");
        stepGoal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String goal = stepGoal.getText().toString();
                    if (!TextUtils.isEmpty(goal)) {
                        int goalStep = new Integer(goal).intValue();
                        if (goalStep > 0 && goalStep < 1000000) {
                            stepGoal.setText(goalStep + "");
                        } else {
                            stepGoal.setText(goalStep);
                            Toast.makeText(ProfileActivity.this, getString
                                    (R.string.profile_user_height_error), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        stepGoal.setText(SpUtils.getIntMethod(ProfileActivity.this, CacheConstants.GOAL_STEP, 10000) + "");
                    }
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_log_out_bt)
    public void logOutClick() {
        showDiaLogMethod(logOut.getId());
    }

    @OnClick(R.id.home_title_right_save_bt)
    public void saveEdit() {
        boolean watchConnected = getModel().getSyncController().isConnected();
        if(!watchConnected){
            noWatchShow.setVisibility(View.VISIBLE);
            AlphaAnimation alpha = new AlphaAnimation(1,0);
            alpha.setDuration(2000);
            alpha.setFillAfter(true);
            noWatchShow.startAnimation(alpha);
            noWatchShow.setLayoutAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    noWatchShow.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        saveUserCurrentEdit();
    }

    private void saveUserCurrentEdit() {
        int currentGoalStep = new Integer(stepGoal.getText().toString()).intValue();
        SpUtils.putIntMethod(ProfileActivity.this, CacheConstants.GOAL_STEP, currentGoalStep);
        mUser.setLastName(accountName.getText().toString());
        mUser.setFirstName(userFirstName.getText().toString());
        mUser.setUserEmail(emailAccount.getText().toString());
        String height = userHeight.getText().toString();
        if (height.contains(getString(R.string.profile_user_height_unit))) {
            int currentHeight = new Integer(height.substring(0, height.length() - 2)).intValue();
            mUser.setHeight(currentHeight);
        } else {
            mUser.setHeight(new Integer(height).intValue());
        }

        String weight = userWeight.getText().toString();
        if (weight.contains(getString(R.string.profile_user_weight_unit))) {
            double currentWeight = Double.parseDouble(weight.substring(0, weight.length() - 2));
            mUser.setWeight(currentWeight);
        } else {
            mUser.setWeight(Double.parseDouble(weight));
        }
        UserDatabaseHelper helper = getModel().getUserDatabaseHelper();
        helper.update(mUser);
    }

    private void showDiaLogMethod(int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.profile_dialog_log_out_prompt).
                    setMessage(R.string.profile_dialog_log_out_message);
            builder.setPositiveButton(R.string.profile_dialog_positive_button_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SpUtils.putBoolean(ProfileActivity.this,CacheConstants.IS_FIRST,true);
                    getModel().getUser().setUserIsLogin(false);
                    getModel().getUserDatabaseHelper().remove(mUser.getUserID(),new Date());
                    Intent intent = getIntent();
                    intent.putExtra("logOut",true);
                    setResult(2>>5,intent);
                    finish();
                }
            });
            builder.setNegativeButton(R.string.profile_dialog_negative_button_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();

//        } else if (id == saveButton.getId()) {
//            builder.setTitle(R.string.profile_dialog_save_title_text).setMessage(R.string.profile_dialog_save_message_text);
//            builder.setPositiveButton(R.string.profile_dialog_save_positive_button_text, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    int currentGoalStep = new Integer(stepGoal.getText().toString()).intValue();
//                    SpUtils.putIntMethod(ProfileActivity.this, CacheConstants.GOAL_STEP, currentGoalStep);
//                    User user = getModel().getUser();
//                    user.setLastName(accountName.getText().toString());
//                    user.setFirstName(userFirstName.getText().toString());
//                    user.setUserEmail(emailAccount.getText().toString());
//                    String height = userHeight.getText().toString();
//                        if (height.contains(getString(R.string.profile_user_height_unit))) {
//                            int currentHeight = new Integer(height.substring(0, height.length() - 2)).intValue();
//                            user.setHeight(currentHeight);
//                        } else {
//                            user.setHeight(new Integer(height).intValue());
//                        }
//
//                    String weight = userWeight.getText().toString();
//                        if (weight.contains(getString(R.string.profile_user_weight_unit))) {
//                            double currentWeight = Double.parseDouble(weight.substring(0, weight.length() - 2));
//                            user.setWeight(currentWeight);
//                        } else {
//                            user.setWeight(Double.parseDouble(weight));
//                        }
//                    UserDatabaseHelper helper = getModel().getUserDatabaseHelper();
//                    if(userStepGoal!=currentGoalStep)
//                    {
//                        userStepGoal = currentGoalStep;
//                        EventBus.getDefault().post(new StepsGoalChangedEvent(userStepGoal));
//                    }
//                    if(helper.update(user))
//                    {
//                        EventBus.getDefault().post(new ProfileChangedEvent(user));
//                    }
//                }
//            });
//
//            builder.setNegativeButton(R.string.profile_dialog_save_negative_button_text, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    dialogInterface.dismiss();
//                }
//            });
//            builder.show();
//        }
    }

    public boolean checkEmail(String email) {
        boolean flag = false;
        try {
            String check = "^([a-z0-9A-Z]+[-|\\\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\\\.)+[a-zA-Z]{2,}$";

            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = getIntent();
            intent.putExtra("logOut",false);
            setResult(2>>5,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
