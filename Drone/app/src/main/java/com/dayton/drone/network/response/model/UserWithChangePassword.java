package com.dayton.drone.network.response.model;

/**
 * Created by Administrator on 2016/6/10.
 */
public class UserWithChangePassword {

    private int id;
    private String last_name;
    private String first_name;
    private UserBirthdayData birthday;
    private int weight;
    private int length;
    private String last_longitude;
    private String last_latitude;
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public UserBirthdayData getBirthday() {
        return birthday;
    }

    public void setBirthday(UserBirthdayData birthday) {
        this.birthday = birthday;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getLast_longitude() {
        return last_longitude;
    }

    public void setLast_longitude(String last_longitude) {
        this.last_longitude = last_longitude;
    }

    public String getLast_latitude() {
        return last_latitude;
    }

    public void setLast_latitude(String last_latitude) {
        this.last_latitude = last_latitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private class UserBirthdayData {

        private String date;
        private int timezone_type;
        private String timezone;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getTimezone_type() {
            return timezone_type;
        }

        public void setTimezone_type(int timezone_type) {
            this.timezone_type = timezone_type;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

    }
}


