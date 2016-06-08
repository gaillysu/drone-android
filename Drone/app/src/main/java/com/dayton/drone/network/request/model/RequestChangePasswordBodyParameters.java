package com.dayton.drone.network.request.model;

/**
 * Created by Administrator on 2016/6/8.
 */
public class RequestChangePasswordBodyParameters {
    private String email;
    private int id;
    private String password_token;
    private  String password;

    public RequestChangePasswordBodyParameters(){

    }

    public RequestChangePasswordBodyParameters(String email,String password_token,String password,int id){
        this.email = email;
        this.id = id;
        this.password = password;
        this.password_token = password_token;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword_token() {
        return password_token;
    }

    public void setPassword_token(String password_token) {
        this.password_token = password_token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
