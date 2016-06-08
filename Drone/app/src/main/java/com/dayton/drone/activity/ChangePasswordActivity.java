package com.dayton.drone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.network.request.RequestChangePasswordRequest;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/8.
 */
public class ChangePasswordActivity extends BaseActivity {

    @Bind(R.id.register_next_iv)
    ImageButton nextButton;

    @Bind(R.id.change_password_new_password)
    EditText firstInputPasswordEditText;

    @Bind(R.id.repeat_edit_password_ed)
    EditText repeatPasswordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_change_password);
        ButterKnife.bind(this);
        nextButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.register_back_iv)
    public void backClick() {
        startActivity(ForgetPasswordActivity.class);
        finish();
    }

    @OnClick(R.id.forget_activity_change_password_bt)
    public void chanagePassword() {
        String newFirstInputPassword = firstInputPasswordEditText.getText().toString();
        String repeatInputPassword = repeatPasswordEditText.getText().toString();
        if (!newFirstInputPassword.isEmpty() && !repeatInputPassword.isEmpty()) {
            if(newFirstInputPassword.equals(repeatInputPassword)){
                ChangePassword(newFirstInputPassword);
            }else{
                repeatPasswordEditText.setError(getString(R.string.change_password_error));
            }
        }
    }

    private void ChangePassword(String newFirstInputPassword) {
        Intent intent  = getIntent();
        int id = intent.getIntExtra("id",-1);
        String email = intent.getStringExtra("email");
        String password_token  =intent.getStringExtra("token");
        getModel().getRetrofitManager().execute(new RequestChangePasswordRequest(password_token, email, newFirstInputPassword, id), new RequestListener() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
            }

            @Override
            public void onRequestSuccess(Object o) {

            }
        });

    }
}
