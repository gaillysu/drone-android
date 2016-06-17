package com.dayton.drone.activity.tutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.network.request.RequestCheckUserEmailAccount;
import com.dayton.drone.network.request.model.CheckEmailResponse;
import com.dayton.drone.network.request.model.CheckEmailUserBody;
import com.dayton.drone.utils.CheckEmailFormat;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/14.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.register_edit_email_ed)
    EditText ed_email;
    @Bind(R.id.register_edit_password_ed)
    EditText ed_password;
    private String password;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_gender_select_text_color);
        }

        ButterKnife.bind(this);
    }

    @OnClick(R.id.register_back_iv)
    public void goBackActivity() {
        startActivity(WelcomeActivity.class);
        finish();
    }

    @OnClick(R.id.register_next_iv)
    public void nextActivity() {

        email = ed_email.getText().toString();
        password = ed_password.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                ed_email.setError(getString(R.string.tips_user_account_password));
            } else {
                ed_email.setError(null);
            }
            if (password.isEmpty()) {
                ed_password.setError(getString(R.string.tips_user_password));
            } else {
                ed_password.setError(null);
            }

        } else {

            if (CheckEmailFormat.checkEmail(email)) {

                final ProgressDialog progressialog = new ProgressDialog(this);
                progressialog.setIndeterminate(false);
                progressialog.setCancelable(false);
                progressialog.setMessage(getString(R.string.forget_password_dialog_text));
                progressialog.show();

                getModel().getRetrofitManager().execute(new RequestCheckUserEmailAccount(getModel().getRetrofitManager().
                        getAccessToken(), new CheckEmailUserBody(email)), new RequestListener<CheckEmailResponse>() {

                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        progressialog.dismiss();
                        Toast.makeText(RegisterActivity.this, getString(R.string.user_info_weight_error), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRequestSuccess(CheckEmailResponse checkEmailResponse) {
                        progressialog.dismiss();
                        if (checkEmailResponse.getStatus() == -1) {
                            Intent intent = new Intent(RegisterActivity.this, UserInfoActivity.class);
                            intent.putExtra("account", email);
                            intent.putExtra("password", password);
                            startActivity(intent);
                            finish();

                        } else if (checkEmailResponse.getStatus() == 1) {
                            ed_email.setError(RegisterActivity.this.getResources().getString(R.string.register_account_email_error));
                        }else{
                            Toast.makeText(RegisterActivity.this,getString(R.string.network_error_register_email_account)
                                    ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                ed_email.requestFocus();
                ed_email.setError(getString(R.string.register_email_format_error));
            }
        }
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
