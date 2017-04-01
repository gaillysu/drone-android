package com.dayton.drone.model;


public class User {

    private int id = 1;
    private String birthday = "2000-01-01";
    private double weight = 75;
    private int height = 175;
    private int gender = 1;
    private String firstName;
    private String lastName;
    private int strideLength = 60;
    private String userID;
    private String userEmail;
    private String userPassword;
    private boolean userIsLogin;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getStrideLength() {
        return strideLength;
    }

    public void setStrideLength(int strideLength) {
        this.strideLength = strideLength;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public boolean isUserIsLogin() {
        return userIsLogin;
    }

    public void setUserIsLogin(boolean userIsLogin) {
        this.userIsLogin = userIsLogin;
    }
}
