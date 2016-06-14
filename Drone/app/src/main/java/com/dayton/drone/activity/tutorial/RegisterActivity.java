package com.dayton.drone.activity.tutorial;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
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
        String email = ed_email.getText().toString();
        String password = ed_password.getText().toString();
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
            Intent intent = new Intent(RegisterActivity.this, UserInfoActivity.class);
            intent.putExtra("account", email);
            intent.putExtra("password", password);
            startActivity(intent);
            finish();
        }
    }
}
