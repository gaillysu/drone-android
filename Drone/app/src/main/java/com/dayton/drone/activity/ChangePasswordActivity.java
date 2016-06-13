package com.dayton.drone.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.activity.tutorial.WelcomeActivity;
import com.dayton.drone.network.request.RequestChangePasswordRequest;
import com.dayton.drone.network.request.model.ChangePasswordModel;
import com.dayton.drone.network.response.model.RequestChangePasswordResponse;
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
        ChangePasswordModel changePasswordModel = new ChangePasswordModel();
        Intent intent  = getIntent();
        changePasswordModel.setId(intent.getIntExtra("id",-1));
        changePasswordModel.setEmail(intent.getStringExtra("email"));
        changePasswordModel.setPassword_token(intent.getStringExtra("token"));
        changePasswordModel.setPassword(newFirstInputPassword);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.forget_password_dialog_text));
        progressDialog.show();


        getModel().getRetrofitManager().execute(new RequestChangePasswordRequest(getModel()
                .getRetrofitManager().getAccessToken(), changePasswordModel), new RequestListener<RequestChangePasswordResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                progressDialog.dismiss();
                Toast.makeText(ChangePasswordActivity.this,getString(
                        R.string.change_password_forget_password_error),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRequestSuccess(RequestChangePasswordResponse requestChangePasswordResponse) {
                progressDialog.dismiss();
                if(requestChangePasswordResponse.getUser() != null) {
                    startActivity(WelcomeActivity.class);
                    finish();
                }else{
                    Toast.makeText(ChangePasswordActivity.this,getString(
                            R.string.change_password_forget_password_error),Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}
