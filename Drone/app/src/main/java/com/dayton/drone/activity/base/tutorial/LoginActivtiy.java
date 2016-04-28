package com.dayton.drone.activity.base.tutorial;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.HomeActivity;
import com.dayton.drone.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/14.
 */
public class LoginActivtiy extends BaseActivity {

    @Bind(R.id.email_ed)
    EditText ed_account;
    @Bind(R.id.password_ed)
    EditText ed_password;
    @Bind(R.id.login_user)
    Button bt_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_user)
    public void loginClick(){
        String account = ed_account.getText().toString();
        String password = ed_password.getText().toString();
        if (!(TextUtils.isEmpty(account) && TextUtils.isEmpty(password))) {
            //login
            if (userLogin(account, password)) {
                startActivity(HomeActivity.class);
                finish();
            } else {
                Toast.makeText(LoginActivtiy.this,
                        R.string.user_account_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivtiy.this,
                    R.string.tips_user_account_password, Toast.LENGTH_SHORT).show();
        }
    }


    public boolean userLogin(String account, String password) {

        return true;
    }
}
