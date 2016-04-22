package com.dayton.drone.activity.base.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;

/**
 * Created by boy on 2016/4/14.
 */
public class RegisteActivity extends BaseActivity {
    private ImageView iv_back;
    private ImageView iv_next;
    private EditText ed_email;
    private EditText ed_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registe);
        initView();
        addListener();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.registe_back_iv);
        iv_next = (ImageView) findViewById(R.id.registe_next_iv);
        ed_email = (EditText) findViewById(R.id.email_ed);
        ed_password = (EditText) findViewById(R.id.password_ed);
    }

    private void addListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email  = ed_email.getText().toString();
                String password = ed_password.getText().toString();
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    Toast.makeText(RegisteActivity.this,
                            R.string.tips_user_account_password,Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(RegisteActivity.this,UserInfoActivity.class);
                    intent.putExtra("account",email);
                    intent.putExtra("password",password);
                    startActivity(intent);
                }
            }
        });

    }
}
