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

    /**
     * this is the unique ID defined by UTC website,@refer to Java function 'TimeZone.getAvailableIDs()'
     */
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
     * this is used for showing city/zone name, split with ",", current watch only supports english string
     */
    public  static final String fTimeZoneTitle = "timeZoneTitle";
    @DatabaseField
    private String timeZoneTitle;


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

    public String getTimeZoneTitle() {
        return timeZoneTitle;
    }

    public void setTimeZoneTitle(String timeZoneTitle) {
        this.timeZoneTitle = timeZoneTitle;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
