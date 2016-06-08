package com.dayton.drone.network.response.model;

/**
 * Created by Administrator on 2016/6/8.
 */
public class CheckUserEmaLocation {
    private String password_token;
    private String UserEmailAccount;

    public String getPassword_token() {
        return password_token;
    }

    public void setPassword_token(String password_token) {
        this.password_token = password_token;
    }

    public String getUserEmailAccount() {
        return UserEmailAccount;
    }

    public void setUserEmailAccount(String userEmailAccount) {
        UserEmailAccount = userEmailAccount;
    }
}
