package com.dayton.drone.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/8.
 */
public class ChangePasswordActivity extends BaseActivity {

    @Bind(R.id.registe_next_iv)
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

    @OnClick(R.id.registe_back_iv)
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


    }
}
