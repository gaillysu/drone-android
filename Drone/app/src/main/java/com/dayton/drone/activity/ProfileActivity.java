package com.dayton.drone.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
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
import com.dayton.drone.event.ProfileChangedEvent;
import com.dayton.drone.event.StepsGoalChangedEvent;
import com.dayton.drone.model.User;
import com.dayton.drone.network.request.UpdateUserRequest;
import com.dayton.drone.network.request.model.UpdateUser;
import com.dayton.drone.network.response.model.UpdateUserModel;
import com.dayton.drone.utils.CacheConstants;
import com.dayton.drone.utils.CheckEmailFormat;
import com.dayton.drone.utils.SpUtils;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    @Bind(R.id.profile_activity_log_out_bt)
    Button logOut;
    @Bind(R.id.profile_save_no_watch_connected_show)
    RelativeLayout noWatchConnect;
    @Bind(R.id.profile_activity_user_birthday)
    TextView userBirthdayTextView;
    @Bind(R.id.my_toolbar)
    Toolbar mToolbar;

    private int userStepGoal;
    private int viewType = -1;
    private int resultCode = 2 >> 5;
    private ProgressDialog progressDialog;
    private long currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_sex_bg);
        }
        ButterKnife.bind(this);
        initToolbar();
        initView();

    }

    private void initToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        TextView titleText = (TextView) mToolbar.findViewById(R.id.toolbar_title_tv);
        titleText.setText(getString(R.string.home_guide_profile));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("logOut", false);
                setResult(resultCode, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar_save_item, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.toolbar_save_button).setVisible(true);
        return true;
    }

    private void initView() {
        userStepGoal = SpUtils.getIntMethod(this, CacheConstants.GOAL_STEP, 10000);
        stepGoal.setText(userStepGoal + "");
        mUser = getModel().getUser();
        accountName.setText(mUser.getLastName() != null ? mUser.getLastName() :
                getResources().getString(R.string.profile_edit_prompt));
        userFirstName.setText(mUser.getFirstName() != null ? mUser.getFirstName() :
                getResources().getString(R.string.profile_edit_prompt));
        emailAccount.setText(mUser.getUserEmail() != null ? mUser.getUserEmail() :
                getResources().getString(R.string.profile_edit_prompt));
        userBirthdayTextView.setText(mUser.getBirthday());
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

    @OnClick(R.id.profile_activity_edit_user_name_ib)
    public void editUserLastNameOnClick() {
        accountName.requestFocus();
        accountName.setText("");
        CheckEmailFormat.openInputMethod(ProfileActivity.this);
        accountName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    accountName.clearFocus();
                    CheckEmailFormat.closeInputMethod(accountName);
                    String lastName = accountName.getText().toString();
                    if (!TextUtils.isEmpty(lastName)) {

                        mUser.setLastName(lastName);
                        accountName.setText(lastName);
                    } else {
                        if (mUser.getLastName() == null) {
                            accountName.setText(getResources().getString(R.string.profile_edit_prompt));
                            Toast.makeText(ProfileActivity.this, getResources().getString(R.string.profile_prompt_user_edit)
                                    , Toast.LENGTH_SHORT).show();
                        } else {
                            accountName.setText(mUser.getLastName());
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
        CheckEmailFormat.openInputMethod(ProfileActivity.this);
        emailAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    emailAccount.clearFocus();
                    CheckEmailFormat.closeInputMethod(emailAccount);
                    String userEmail = emailAccount.getText().toString();
                    if (!TextUtils.isEmpty(userEmail)) {
                        if (CheckEmailFormat.checkEmail(userEmail)) {
                            mUser.setUserEmail(userEmail);
                            emailAccount.setText(userEmail);
                        } else {
                            emailAccount.setError(getString(R.string.register_email_format_error));
                        }
                    } else {
                        emailAccount.setText(mUser.getUserEmail());
                    }
                }
            }
        });
    }


    @OnClick(R.id.profile_activity_edit_fist_name_ib)
    public void editUserFirstName() {
        userFirstName.requestFocus();
        userFirstName.setText("");
        CheckEmailFormat.openInputMethod(ProfileActivity.this);
        userFirstName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if (!isFocus) {
                    userFirstName.clearFocus();
                    CheckEmailFormat.closeInputMethod(userFirstName);
                    String firstName = userFirstName.getText().toString();
                    if (!TextUtils.isEmpty(firstName)) {
                        mUser.setFirstName(firstName);
                        userFirstName.setText(firstName);
                    } else {
                        if (mUser.getFirstName() == null) {
                            userFirstName.setText(getResources().getString(R.string.profile_edit_prompt));
                            Toast.makeText(ProfileActivity.this, getString(R.string.profile_prompt_user_edit),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            userFirstName.setText(mUser.getFirstName());
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_user_height)
    public void editUserHeight() {
        userHeight.setText("");
        viewType = 2;
        stepGoal.clearFocus();
        emailAccount.clearFocus();
        userFirstName.clearFocus();
        accountName.clearFocus();
        DatePickerPopWin pickerPopWin2 = new DatePickerPopWin.Builder(ProfileActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {
                        userHeight.setText(dateDesc + getResources().getString(R.string.profile_user_height_unit));
                    }
                }).viewStyle(viewType)
                .viewTextSize(25)
                .dateChose(mUser.getHeight() + "")
                .build();
        pickerPopWin2.showPopWin(ProfileActivity.this);
        pickerPopWin2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                String height = userHeight.getText().toString();
                if (TextUtils.isEmpty(height)) {
                    userHeight.setText(mUser.getHeight() + getResources().getString(R.string.profile_user_height_unit));
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_user_weight)
    public void editUserWeight() {
        userWeight.setText("");
        viewType = 3;
        stepGoal.clearFocus();
        emailAccount.clearFocus();
        userFirstName.clearFocus();
        accountName.clearFocus();

        DatePickerPopWin pickerPopWin3 = new DatePickerPopWin.Builder(ProfileActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {
                        userWeight.setText(dateDesc + getResources().getString(R.string.profile_user_weight_unit));
                    }
                }).viewStyle(viewType)
                .viewTextSize(25)
                .dateChose(new Double(mUser.getWeight()).intValue() + "")
                .build();
        pickerPopWin3.showPopWin(ProfileActivity.this);
        pickerPopWin3.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                String weight = userWeight.getText().toString();
                if (TextUtils.isEmpty(weight)) {
                    userWeight.setText(mUser.getWeight() + getResources().getString(R.string.profile_user_weight_unit));
                }
            }
        });
    }

    @OnClick(R.id.profile_activity_edit_goal_ib)
    public void editGoalClick() {
        stepGoal.requestFocus();
        stepGoal.setText("");
        CheckEmailFormat.openInputMethod(ProfileActivity.this);
        stepGoal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {

                    CheckEmailFormat.closeInputMethod(stepGoal);
                    String goal = stepGoal.getText().toString();
                    if (!TextUtils.isEmpty(goal)) {
                        int goalStep = Integer.valueOf(goal);
                        if (goalStep > 0 && goalStep < 10000000) {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_save_button) {
            boolean watchConnected = getModel().getSyncController().isConnected();
            if (!watchConnected) {
                noWatchConnect.setVisibility(View.VISIBLE);
                AlphaAnimation alpha = new AlphaAnimation(1, 0);
                alpha.setDuration(2000);
                alpha.setFillAfter(true);
                alpha.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        noWatchConnect.setVisibility(View.GONE);
                        saveUserCurrentEdit();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                noWatchConnect.startAnimation(alpha);
            } else {

                currentTime = System.currentTimeMillis();
                progressDialog = new ProgressDialog(this);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.forget_password_dialog_text));
                progressDialog.show();
                saveUserCurrentEdit();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.profile_activity_edit_user_birthday)
    public void editUserBirthday() {
        userBirthdayTextView.setText("");
        viewType = 1;
        final Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String formatDate = format.format(date);
        DatePickerPopWin pickerPopWin = new DatePickerPopWin.Builder(ProfileActivity.this,
                new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month,
                                                    int day, String dateDesc) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            Date userSelectDate = dateFormat.parse(dateDesc);
                            userBirthdayTextView.setText(new SimpleDateFormat("MMM").format(userSelectDate) + "-" + day + "-" + year);
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
        pickerPopWin.showPopWin(ProfileActivity.this);
    }

    public void saveUserCurrentEdit() {
        final int currentGoalStep = Integer.valueOf(stepGoal.getText().toString());
        SpUtils.putIntMethod(ProfileActivity.this, CacheConstants.GOAL_STEP, currentGoalStep);
        mUser.setLastName(accountName.getText().toString());
        mUser.setFirstName(userFirstName.getText().toString());
        mUser.setUserEmail(emailAccount.getText().toString());
        mUser.setBirthday(userBirthdayTextView.getText().toString());
        String height = userHeight.getText().toString();
        if (height.contains(getString(R.string.profile_user_height_unit))) {
            int currentHeight = Integer.valueOf(height.substring(0, height.length() - 2));
            mUser.setHeight(currentHeight);
        } else {
            mUser.setHeight(Integer.valueOf(height));
        }

        String weight = userWeight.getText().toString();
        if (weight.contains(getString(R.string.profile_user_weight_unit))) {
            double currentWeight = Double.parseDouble(weight.substring(0, weight.length() - 2));
            mUser.setWeight(currentWeight);
        } else {
            mUser.setWeight(Double.parseDouble(weight));
        }
        UserDatabaseHelper helper = getModel().getUserDatabaseHelper();
        if (helper.update(mUser)) {
            EventBus.getDefault().post(new ProfileChangedEvent(mUser));
        }
        if (userStepGoal != currentGoalStep) {
            userStepGoal = currentGoalStep;
            EventBus.getDefault().post(new StepsGoalChangedEvent(userStepGoal));
        }

        UpdateUser updateUser = new UpdateUser();
        updateUser.setId(Integer.parseInt(mUser.getUserID()));
        updateUser.setFirst_name(mUser.getFirstName());
        updateUser.setLast_name(mUser.getLastName());
        updateUser.setEmail(mUser.getUserEmail());
        updateUser.setLength(mUser.getHeight());
        updateUser.setBirthday(mUser.getBirthday());
        updateUser.setSex(mUser.getGender());
        getModel().getRetrofitManager().execute(new UpdateUserRequest(updateUser, getModel()
                .getRetrofitManager().getAccessToken()), new RequestListener<UpdateUserModel>() {

            @Override
            public void onRequestFailure(SpiceException spiceException) {
                if (System.currentTimeMillis() - currentTime < 2000) {
                    SystemClock.sleep(2000 - (System.currentTimeMillis() - currentTime));
                }
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                Toast.makeText(ProfileActivity.this, getString(R.string.save_failed_prompt), Toast.LENGTH_SHORT).show();
                spiceException.printStackTrace();
            }

            @Override
            public void onRequestSuccess(UpdateUserModel updateUserModel) {
                if (System.currentTimeMillis() - currentTime < 2000) {
                    SystemClock.sleep(2000 - (System.currentTimeMillis() - currentTime));

                }

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (updateUserModel.getStatus() == 1) {
                    Intent intent = getIntent();
                    intent.putExtra("logOut", false);
                    setResult(resultCode, intent);
                    finish();
                }

            }
        });

    }

    private void showDiaLogMethod(int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.profile_dialog_log_out_prompt)).
                setMessage(getString(R.string.profile_dialog_log_out_message));
        builder.setPositiveButton(getString(R.string.profile_dialog_positive_button_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SpUtils.putIntMethod(ProfileActivity.this, CacheConstants.GOAL_STEP, 10000);
                        getModel().getUser().setUserIsLogin(false);
                        getModel().getUserDatabaseHelper().update(getModel().getUser());
                        Intent intent = getIntent();
                        intent.putExtra("logOut", true);
                        setResult(resultCode, intent);
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = getIntent();
            intent.putExtra("logOut", false);
            setResult(resultCode, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
