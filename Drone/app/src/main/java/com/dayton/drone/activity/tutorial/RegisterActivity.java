package com.dayton.drone.activity.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/14.
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.email_ed)
    EditText ed_email;
    @Bind(R.id.password_ed)
    EditText ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.registe_back_iv)
    public void goBackActivity() {
        startActivity(WelcomeActivity.class);
        finish();
    }

    @OnClick(R.id.registe_next_iv)
    public void nextActivity() {
        String email = ed_email.getText().toString();
        String password = ed_password.getText().toString();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this,
                    R.string.tips_user_account_password, Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(RegisterActivity.this, UserInfoActivity.class);
            intent.putExtra("account", email);
            intent.putExtra("password", password);
            startActivity(intent);
            finish();
        }
    }
}
