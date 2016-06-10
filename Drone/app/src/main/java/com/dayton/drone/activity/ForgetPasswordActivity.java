package com.dayton.drone.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.activity.tutorial.WelcomeActivity;
import com.dayton.drone.network.request.RequestTokenRequest;
import com.dayton.drone.network.response.model.RequestTokenResponse;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/6/8.
 */
public class ForgetPasswordActivity extends BaseActivity {

    @Bind(R.id.forget_activity_edit_change_password_email_et)
    EditText emailAddressEdit;

    @Bind(R.id.register_next_iv)
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
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getString(R.string.forget_password_dialog_text));
            progressDialog.show();

            getModel().getRetrofitManager().execute(new RequestTokenRequest(getModel()
                    .getRetrofitManager().getAccessToken(), changePasswordEmail), new RequestListener<RequestTokenResponse>() {
                @Override
                public void onRequestFailure(SpiceException spiceException) {
                    progressDialog.dismiss();
                    emailAddressEdit.setError(getString(R.string.email_is_error));
                }

                @Override
                public void onRequestSuccess(RequestTokenResponse requestTokenResponse) {
                    if(requestTokenResponse != null) {
                        String email = requestTokenResponse.getUser().getEmail();
                        String token = requestTokenResponse.getUser().getPassword_token();
                        int id = requestTokenResponse.getUser().getId();

                        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(token)) {
                            Intent intent = new Intent(ForgetPasswordActivity.this, ChangePasswordActivity.class);
                            intent.putExtra("email", email);
                            intent.putExtra("token", token);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        } else {
                            emailAddressEdit.setError(getString(R.string.forget_password_request_null));
                        }
                    }
                    progressDialog.dismiss();
                }
            });
        } else {
            emailAddressEdit.setError(getString(R.string.tips_user_account_password));
        }


    }

    @OnClick(R.id.register_back_iv)
    public void backLOginPage() {
        startActivity(WelcomeActivity.class);
        finish();
    }

}
