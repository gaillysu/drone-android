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
     * from "A"  to "Z"
     */
    public  static final String fTimeZoneCategory = "timeZoneCategory";
    @DatabaseField
    private String timeZoneCategory;

    /**
     * from -12.0 to +12.0
     */
    public  static final String fTimeZoneOffset = "timeZoneOffset";
    @DatabaseField
    private float timeZoneOffset;

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

    public String getTimeZoneCategory() {
        return timeZoneCategory;
    }

    public void setTimeZoneCategory(String timeZoneCategory) {
        this.timeZoneCategory = timeZoneCategory;
    }

    public float getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(float timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
