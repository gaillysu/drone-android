package com.dayton.drone.database.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by boy on 2016/4/12.
 */

@DatabaseTable(tableName = "tb_user")
public class UserBean {

    /**
     * field name and initialize value, Primary field
     */
    public static final String fID = "ID";
    @DatabaseField(generatedId = true)
    private int ID = 1;

    /**
     * milliseconds since January 1, 1970, 00:00:00 GMT
     * for sample: Nov 17,2000,18:50:20
     */
    public static final String fBirthday = "Birthday";
    @DatabaseField
    private long Birthday;

    /**
     * default age
     */
    public static final String fAge = "Age";
    @DatabaseField
    private int Age = 35;

    /**
     * default weight, 75kg
     */
    public static final String fWeight = "Weight";
    @DatabaseField
    private int Weight = 75;

    /**
     * default height, 175cm
     */
    public static final String fHeight = "Height";
    @DatabaseField
    private int Height = 175;

    /**
     * Sex = 0, female, sex = 1, male
     */
    public static  final String fSex = "Sex";
    @DatabaseField
    private int Sex = 1;

    /**
     * first name
     */
    public  static final String fFirstName = "FirstName";
    @DatabaseField
    private String FirstName;

    /**
     * last name
     */
    public static final String fLastName = "LastName";
    @DatabaseField
    private String LastName;

    /**
     * created date
     */
    public static final String fCreatedDate = "CreatedDate";
    @DatabaseField
    private long CreatedDate;

    /**
     * remarks field, save extend user infomation, such as: blood type, email,step length...
     * it is a Json string
     */

    public static final String fRemarks = "Remarks";
    @DatabaseField
    private String Remarks;

    /**
     * this is generated by Nevo API when register
     */
    public static final String fNevoUserID = "nevoUserID";
    @DatabaseField
    private String nevoUserID;

    /**
     * this is generated by Nevo API when register
     */
    public static final String fNevoUserToken = "nevoUserToken";
    @DatabaseField
    private String nevoUserToken;

    public static final String fNevoUserEmail = "nevoUserEmail";
    @DatabaseField
    private String nevoUserEmail;
    /**
     * this is generated by Validic API when create user
     */
    public static final String fValidicUserID = "validicUserID";
    @DatabaseField
    private String validicUserID;

    /**
     * this is generated by Validic API when create user
     */
    public static final String fValidicUserToken = "validicUserToken";
    @DatabaseField
    private String validicUserToken;

    public static final String fNevoUserIsLogin = "nevoUserIsLogin";
    @DatabaseField
    private boolean nevoUserIsLogin;

    public static final String fValidicUserIsConnected = "validicUserIsConnected";
    @DatabaseField
    private boolean validicUserIsConnected;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public long getBirthday() {
        return Birthday;
    }

    public void setBirthday(long birthday) {
        Birthday = birthday;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int weight) {
        Weight = weight;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public long getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(long createdDate) {
        CreatedDate = createdDate;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getUserID() {
        return nevoUserID;
    }

    public void setUserID(String nevoUserID) {
        this.nevoUserID = nevoUserID;
    }

    public String getUserToken() {
        return nevoUserToken;
    }

    public void setUserToken(String nevoUserToken) {
        this.nevoUserToken = nevoUserToken;
    }

    public String getUserEmail() {
        return nevoUserEmail;
    }

    public void setUserEmail(String nevoUserEmail) {
        this.nevoUserEmail = nevoUserEmail;
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

    public boolean isUserIsLogin() {
        return nevoUserIsLogin;
    }

    public void setUserIsLogin(boolean nevoUserIsLogin) {
        this.nevoUserIsLogin = nevoUserIsLogin;
    }

    public boolean isValidicUserIsConnected() {
        return validicUserIsConnected;
    }

    public void setValidicUserIsConnected(boolean validicUserIsConnected) {
        this.validicUserIsConnected = validicUserIsConnected;
    }
}