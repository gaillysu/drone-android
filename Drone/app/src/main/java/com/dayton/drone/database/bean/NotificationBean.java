package com.dayton.drone.database.bean;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by med on 16/5/9.
 */
public class NotificationBean {
    /**
     * field name and initialize value, Primary field
     */
    public static final String fID = "id";
    @DatabaseField(generatedId = true)
    private int id = 1;

    public static final String fApplication = "application";
    @DatabaseField
    private String application;

    /**
     * black/white list mode
     */
    public static final String fOperationMode = "operationMode";
    @DatabaseField
    private int operationMode;


    /**
     * contacts list, split by ";"
     */
    public static final String fContactsList = "contactsList";
    @DatabaseField
    private String contactsList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public int getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(int operationMode) {
        this.operationMode = operationMode;
    }

    public String getContactsList() {
        return contactsList;
    }

    public void setContactsList(String contactsList) {
        this.contactsList = contactsList;
    }
}
