package com.dayton.drone.database.bean;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by med on 16/5/9.
 */
public class WorldClockBean {
    /**
     * field name and initialize value, Primary field
     */
    public static final String fID = "id";
    @DatabaseField(generatedId = true)
    private int id = 1;

    public  static final String fTimeZoneName = "timeZoneName";
    @DatabaseField
    private String timeZoneName;

    /**
     * if "1", show it in the world clock list
     */
    public  static final String fSelected = "selected";
    @DatabaseField
    private int selected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeZoneName() {
        return timeZoneName;
    }

    public void setTimeZoneName(String timeZoneName) {
        this.timeZoneName = timeZoneName;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
