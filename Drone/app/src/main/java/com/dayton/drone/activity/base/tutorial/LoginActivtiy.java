package com.dayton.drone.activity.base.tutorial;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.dayton.drone.R;
import com.dayton.drone.activity.base.BaseActivity;
import com.dayton.drone.network.request.model.LoginUser;
import com.dayton.drone.network.response.model.LoginUserModel;
import com.dayton.drone.network.request.LoginUserRequest;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //TODO this is test hardcode
        ed_account.setText("test@med-corp.net");
        ed_password.setText("123456");
    }

    @OnClick(R.id.registe_back_iv)
    public void back(){
        startActivity(TutorialActivtiy.class);
        finish();
    }

    @OnClick(R.id.registe_next_iv)
    public void loginClick(){
         if(!validate()){
             onLoginFailed();
             return;
         }
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivtiy.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("logging in...");
        progressDialog.show();

        LoginUser userLogin = new LoginUser();
         userLogin.setEmail(ed_account.getText().toString());
         userLogin.setPassword(ed_password.getText().toString());
         getModel().getRetrofitManager().execute(new LoginUserRequest(userLogin,getModel().getRetrofitManager().getAccessToken()),new RequestListener<LoginUserModel>(){

             @Override
             public void onRequestFailure(SpiceException spiceException) {
                 progressDialog.dismiss();
                 onLoginFailed();
             }

             @Override
             public void onRequestSuccess(LoginUserModel loginUserModel) {
                 progressDialog.dismiss();
                 if(loginUserModel.getStatus()==1) {
                     getModel().getUser().setUserID(loginUserModel.getUser().getId()+"");
                     getModel().getUser().setUserEmail(loginUserModel.getUser().getEmail());
                     getModel().getUser().setUserIsLogin(true);
                     getModel().getUserDatabaseHelper().update(getModel().getUser());
                     getModel().getSyncActivityManager().launchSyncAll();
                     onLoginSuccess();
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

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), "log in got failed", Toast.LENGTH_LONG).show();
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
            ed_password.setError(getString(R.string.tips_user_account_password));
            valid = false;
        } else {
            ed_password.setError(null);
        }
        return valid;
    }

}
