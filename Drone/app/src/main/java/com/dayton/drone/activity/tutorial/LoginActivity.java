package com.dayton.drone.activity.tutorial;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.network.request.LoginUserRequest;
import com.dayton.drone.network.request.model.LoginUser;
import com.dayton.drone.network.response.model.LoginUserModel;
import com.dayton.drone.utils.CheckEmailFormat;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import net.medcorp.library.permission.PermissionRequestDialogBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/14.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.register_edit_email_ed)
    EditText ed_account;
    @Bind(R.id.register_edit_password_ed)
    EditText ed_password;
    @Bind(R.id.register_next_iv)
    ImageButton nextImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_gender_select_text_color);
        }
        PermissionRequestDialogBuilder builder =new PermissionRequestDialogBuilder(this);
        builder.addPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
        builder.askForPermission(this,1);

        ButterKnife.bind(this);
        nextImageButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.register_back_iv)
    public void back() {
        startActivity(WelcomeActivity.class);
        getModel().getUser().setUserIsLogin(false);
        finish();
    }

    @OnClick(R.id.login_activity_login_bt)
    public void loginClick() {
        if (!validate()) {
            //             onLoginFailed("invalid email or password");
            return;
        }
        String email = ed_account.getText().toString();
        String password = ed_password.getText().toString();
        if (CheckEmailFormat.checkEmail(ed_account.getText().toString())) {
            ed_account.setError(null);
            final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.user_login_dialog_text));
            progressDialog.show();

                        LoginUser userLogin = new LoginUser();
                        userLogin.setEmail(email);
                        userLogin.setPassword(password);
                        getModel().getRetrofitManager().execute(new LoginUserRequest(userLogin,
                                getModel().getRetrofitManager().getAccessToken()), new RequestListener<LoginUserModel>() {

                            @Override
                            public void onRequestFailure(SpiceException spiceException) {
                                progressDialog.dismiss();
                                onLoginFailed(spiceException.getMessage());
                            }

                            @Override
                            public void onRequestSuccess(LoginUserModel loginUserModel) {
                                progressDialog.dismiss();
                                if (loginUserModel.getStatus() == 1) {
                                    getModel().getUser().setUserID(loginUserModel.getUser().getId() + "");
                                    getModel().getUser().setUserEmail(loginUserModel.getUser().getEmail());
                                    getModel().getUser().setUserPassword(ed_password.getText().toString());
                                    getModel().getUser().setFirstName(loginUserModel.getUser().getFirst_name());
                                    getModel().getUser().setLastName(loginUserModel.getUser().getLast_name());
                                    getModel().getUser().setUserIsLogin(true);
                                    getModel().getUserDatabaseHelper().update(getModel().getUser());
                                    getModel().getSyncActivityManager().launchSyncAll();

                                    onLoginSuccess();
                                } else {
                                    Log.e("LoginActivity", loginUserModel.getMessage() + ",state:" + loginUserModel.getStatus());
                                    onLoginFailed(loginUserModel.getMessage());
                                }
                            }
                        });
        } else {
            ed_account.setError(getString(R.string.register_email_format_error));
        }
    }

    private void onLoginSuccess() {
        Toast.makeText(getBaseContext(), getString(R.string.login_success_toast_text), Toast.LENGTH_LONG).show();
        Intent loginIntent = new Intent(this, SelectDeviceActivity.class);
        int type = 4;
        loginIntent.putExtra("type", type);
        startActivity(loginIntent);
        finish();
    }

    private void onLoginFailed(String cause) {
        Toast.makeText(getBaseContext(), getString(R.string.login_failed_toast_text) + "\n" + cause, Toast.LENGTH_LONG).show();
    }

    private boolean validate() {
        boolean valid = true;
        String email = ed_account.getText().toString();
        String password = ed_password.getText().toString();
        if (email.isEmpty()) {
            ed_account.setError(getString(R.string.tips_user_account_password));
            valid = false;
        } else {
            ed_account.setError(null);
        }
        if (password.isEmpty()) {
            ed_password.setError(getString(R.string.tips_user_password));
            valid = false;
        } else {
            ed_password.setError(null);
        }
        return valid;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(WelcomeActivity.class);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
