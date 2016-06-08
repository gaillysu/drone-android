package com.dayton.drone.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.activity.tutorial.WelcomeActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/8.
 */
public class ForgetPasswordActivity extends BaseActivity {

    @Bind(R.id.forget_activity_edit_change_password_email_et)
    EditText emailAddressEdit;
    @Bind(R.id.register_back_iv)
    ImageButton nextPageImageButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_layout);
        ButterKnife.bind(this);
        nextPageImageButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.forget_activity_change_password_bt)
    public void changePasswordClick() {
        String changePasswordEmail = emailAddressEdit.getText().toString();
        if (!changePasswordEmail.isEmpty()) {
            if (checkEmailIsRegister(changePasswordEmail)) {
                startActivity(ChangePasswordActivity.class);
                finish();
            } else {
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        }else{
            emailAddressEdit.setError(getString(R.string.tips_user_account_password));
        }
    }

    private boolean checkEmailIsRegister(String changePasswordEmail) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(getString(R.string.forget_password_dialog_text));
        progressDialog.show();
        
        return true;
    }

    @OnClick(R.id.register_back_iv)
    public void backLOginPage() {
        startActivity(WelcomeActivity.class);
        finish();
    }

}
