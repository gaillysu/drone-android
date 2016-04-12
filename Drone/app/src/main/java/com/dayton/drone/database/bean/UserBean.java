package com.dayton.drone.database.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by boy on 2016/4/12.
 */

@DatabaseTable(tableName = "tb_user")
public class UserBean {


    @DatabaseField(generatedId = true)
    private int ID = 1;

    @DatabaseField(columnName = "birthday")
    private long birthday;

    @DatabaseField(columnName = "age")
    private int age = 35;


    @DatabaseField(columnName = "weight")
    private int weight = 75;

    @DatabaseField(columnName = "height")
    private int height = 175;

    /**
     * Sex = 0, female, sex = 1, male
     */
    @DatabaseField(columnName = "sex")
    private int sex = 1;

    @DatabaseField(columnName = "firstName")
    private String firstName;

    @DatabaseField(columnName = "lastName")
    private String lastName;

    @DatabaseField(columnName = "createdDate")
    private long createdDate;

    @DatabaseField(columnName = "remarks")
    private String remarks;

    @DatabaseField(columnName = "droneUserID")
    private String droneUserId;

    @DatabaseField(columnName = "droneUserToken")
    private String droneUserToken;

    @DatabaseField(columnName = "droenUserEmail")
    private String droenUserEmail;

    @DatabaseField(columnName = "validicUserID")
    private String validicUserID;

    @DatabaseField(columnName = "validicUserToken")
    private String validicUserToken;

    @DatabaseField(columnName = "droneUserIsLogin")
    private boolean droneUserIsLogin;

    @DatabaseField(columnName = "validicUserIsConnected")
    private boolean validicUserIsConnected;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
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

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDroneUserId() {
        return droneUserId;
    }

    public void setDroneUserId(String droneUserId) {
        this.droneUserId = droneUserId;
    }

    public String getDroneUserToken() {
        return droneUserToken;
    }

    public void setDroneUserToken(String droneUserToken) {
        this.droneUserToken = droneUserToken;
    }

    public String getDroenUserEmail() {
        return droenUserEmail;
    }

    public void setDroenUserEmail(String droenUserEmail) {
        this.droenUserEmail = droenUserEmail;
    }

    public String getValidicUserID() {
        return validicUserID;
    }

    public void setValidicUserID(String validicUserID) {
        this.validicUserID = validicUserID;
    }

    public String getValidicUserToken() {
        return validicUserToken;
    }

    public void setValidicUserToken(String validicUserToken) {
        this.validicUserToken = validicUserToken;
    }

    public boolean isDroneUserIsLogin() {
        return droneUserIsLogin;
    }

    public void setDroneUserIsLogin(boolean droneUserIsLogin) {
        this.droneUserIsLogin = droneUserIsLogin;
    }

    public boolean isValidicUserIsConnected() {
        return validicUserIsConnected;
    }

    public void setValidicUserIsConnected(boolean validicUserIsConnected) {
        this.validicUserIsConnected = validicUserIsConnected;
    }

}
