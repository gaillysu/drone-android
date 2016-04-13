package com.dayton.drone.database.bean;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by boy on 2016/4/13.
 */
public class AlarmBean {

    @DatabaseField(generatedId = true)
    private int id = 1;
    @DatabaseField(columnName = "alarmName")
    private String alarmName ="";
    @DatabaseField(columnName = "lable")
    private String lable;
    @DatabaseField(columnName = "enable")
    private boolean anable;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlarmName() {
        return alarmName;
    }

    public void setAlarmName(String alarmName) {
        this.alarmName = alarmName;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }

    public boolean isAnlable() {
        return anable;
    }

    public void setAnlable(boolean anlable) {
        this.anable = anlable;
    }
}
