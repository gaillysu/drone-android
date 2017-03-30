package com.dayton.drone.database.bean;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by med on 17/3/30.
 */

public class UserBean extends RealmObject {
    /**
     * field name and initialize value, Primary field
     */
    @Ignore
    public static final String fID = "id";
    private int id = 1;

    /**
     * user birthday, "YYYY-MM-DD"
     */
    @Ignore
    public static final String fBirthday = "birthday";
    private String birthday = "2000-01-01";

    /**
     * default weight, 75kg
     */
    @Ignore
    public static final String fWeight = "weight";
    private double weight = 75;

    /**
     * default height, 175cm
     */
    @Ignore
    public static final String fHeight = "height";
    private int height = 175;

    /**
     * gender = 0, female, gender = 1, male
     */
    @Ignore
    public static  final String fGender = "gender";
    private int gender = 1;

    /**
     * first name
     */
    @Ignore
    public  static final String fFirstName = "firstName";
    private String firstName;

    /**
     * last name
     */
    @Ignore
    public static final String fLastName = "lastName";
    private String lastName;

    /**
     * average stride length in "CM",default 60cm
     */
    @Ignore
    public static final String fStrideLength = "strideLength";
    private int strideLength = 60;

    /**
     * this is generated by Drone API when register
     */
    @Ignore
    public static final String fUserID = "userID";
    private String userID;

    @Ignore
    public static final String fUserEmail = "userEmail";
    private String userEmail;

    @Ignore
    public static final String fUserPassword = "userPassword";
    private String userPassword;

    @Ignore
    public static final String fUserIsLogin = "userIsLogin";
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
