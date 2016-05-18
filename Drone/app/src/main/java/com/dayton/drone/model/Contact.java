package com.dayton.drone.model;

/**
 * Created by med on 16/5/18.
 */
public class Contact {
    private String name;
    private String number; //for more number, split with ";"

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
