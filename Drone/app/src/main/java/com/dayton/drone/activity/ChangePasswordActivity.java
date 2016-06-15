package com.dayton.drone.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
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
import com.readystatesoftware.systembartint.SystemBarTintManager;

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

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_change_password);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.user_info_gender_select_text_color);
        }

        ButterKnife.bind(this);
        nextButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.register_back_iv)
    public void backButtonClick() {
        startActivity(ForgetPasswordActivity.class);
        finish();
    }

    @OnClick(R.id.forget_activity_change_password_bt)
    public void changePasswordClick() {
        String newFirstInputPassword = firstInputPasswordEditText.getText().toString();
        String repeatInputPassword = repeatPasswordEditText.getText().toString();
        if (!newFirstInputPassword.isEmpty() && !repeatInputPassword.isEmpty()) {
            if(newFirstInputPassword.equals(repeatInputPassword)){
                ChangePasswordModel changePasswordModel = new ChangePasswordModel();
                Intent intent  = getIntent();
                changePasswordModel.setId(intent.getIntExtra("id",-1));
                changePasswordModel.setEmail(intent.getStringExtra("email"));
                changePasswordModel.setPassword_token(intent.getStringExtra("token"));
                changePasswordModel.setPassword(newFirstInputPassword);

                progressDialog = new ProgressDialog(this);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.setMessage(getString(R.string.forget_password_dialog_text));
                progressDialog.show();
                getModel().getRetrofitManager().execute(new RequestChangePasswordRequest(getModel()
                        .getRetrofitManager().getAccessToken(), changePasswordModel), responseRequestListener);
            }else{
                repeatPasswordEditText.setError(getString(R.string.change_password_error));
            }
        }
    }
    
    private RequestListener<RequestChangePasswordResponse> responseRequestListener = new RequestListener<RequestChangePasswordResponse>() {
        @Override
        public void onRequestFailure(SpiceException spiceException) {
            progressDialog.dismiss();
        }

        @Override
        public void onRequestSuccess(RequestChangePasswordResponse requestChangePasswordResponse) {
            progressDialog.dismiss();
            startActivity(WelcomeActivity.class);
            finish();
            Toast.makeText(ChangePasswordActivity.this, R.string.password_password_changed, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(ForgetPasswordActivity.class);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
