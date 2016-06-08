package com.dayton.drone.activity.tutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.network.request.LoginUserRequest;
import com.dayton.drone.network.request.model.LoginUser;
import com.dayton.drone.network.response.model.LoginUserModel;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by boy on 2016/4/14.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.register_edit_email_ed)
    EditText ed_account;
    @Bind(R.id.register_edit_password_ed)
    EditText ed_password;
    @Bind(R.id.registe_next_iv)
    ImageButton nextImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        nextImageButton.setVisibility(View.GONE);
    }

    @OnClick(R.id.registe_back_iv)
    public void back(){
        startActivity(WelcomeActivity.class);
        getModel().getUser().setUserIsLogin(false);
        finish();
    }

    @OnClick(R.id.login_activity_login_bt)
    public void loginClick(){
         if(!validate()){
//             onLoginFailed("invalid email or password");
             return;
         }
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("logging in...");
        progressDialog.show();

        LoginUser userLogin = new LoginUser();
         userLogin.setEmail(ed_account.getText().toString());
         userLogin.setPassword(ed_password.getText().toString());
         getModel().getRetrofitManager().execute(new LoginUserRequest(userLogin,
                 getModel().getRetrofitManager().getAccessToken()),new RequestListener<LoginUserModel>(){

             @Override
             public void onRequestFailure(SpiceException spiceException) {
                 progressDialog.dismiss();
                 onLoginFailed(""+spiceException.getCause());
             }

             @Override
             public void onRequestSuccess(LoginUserModel loginUserModel) {
                 progressDialog.dismiss();
                 if(loginUserModel.getStatus()==1) {
                     getModel().getUser().setUserID(loginUserModel.getUser().getId()+"");
                     getModel().getUser().setUserEmail(loginUserModel.getUser().getEmail());
                     getModel().getUser().setUserPassword(ed_password.getText().toString());
                     getModel().getUser().setUserIsLogin(true);
                     getModel().getUserDatabaseHelper().update(getModel().getUser());
                     getModel().getSyncActivityManager().launchSyncAll();
                     onLoginSuccess();
                 }
                 else{
                     Log.e("LoginActivity",loginUserModel.getMessage() + ",state:" + loginUserModel.getStatus());
                     onLoginFailed(loginUserModel.getMessage());
                 }
             }
         });
    }

    private void onLoginSuccess() {
        Toast.makeText(getBaseContext(), "log in success", Toast.LENGTH_LONG).show();
        Intent loginIntent = new Intent(this,SelectDeviceActivity.class);
        int type = 2>>4;
        loginIntent.putExtra("type",type);
        startActivity(loginIntent);
        finish();
    }

    private void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), "log in got failed," + message, Toast.LENGTH_LONG).show();
    }

    private boolean validate() {
        boolean valid = true;
        String email = ed_account.getText().toString();
        String password = ed_password.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed_account.setError(getString(R.string.tips_user_account_password));
            valid = false;
        } else {
            ed_account.setError(null);
        }
        if (password.isEmpty()) {
            ed_password.setError(getString(R.string.tips_user_password));
            valid = false;
        } else {
            ed_password.setError(null);
        }
        return valid;
    }

}
