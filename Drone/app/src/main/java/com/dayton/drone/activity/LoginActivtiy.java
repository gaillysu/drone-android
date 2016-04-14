package com.dayton.drone.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;

/**
 * Created by boy on 2016/4/14.
 */
public class LoginActivtiy extends BaseActivity {
    private EditText ed_account ;
    private EditText ed_password;
    private Button bt_Login;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        addListener();
    }

    private void initView() {
        ed_account = (EditText)findViewById(R.id.email_ed);
        ed_password = (EditText) findViewById(R.id.password_ed);
        bt_Login = (Button) findViewById(R.id.login_user);
    }

    public void addListener(){
        bt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = ed_account.getText().toString();
                String password = ed_password.getText().toString();

                if(TextUtils.isEmpty(account) && TextUtils.isEmpty(password)){
                    //login
                   if(userLogin(account , password)){
                       startActivity(MainActivity.class);
                       finish();
                   }else{
                       Toast.makeText(LoginActivtiy.this,
                               R.string.user_account_error,Toast.LENGTH_SHORT).show();
                   }
                }else{
                    Toast.makeText(LoginActivtiy.this,
                            R.string.tips_user_account_password,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean userLogin(String account,String password){

        return true;
    }
}
