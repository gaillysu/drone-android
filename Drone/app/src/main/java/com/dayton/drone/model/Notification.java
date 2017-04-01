package com.dayton.drone.model;


/**
 * Created by med on 16/5/9.
 */
public class Notification {

    private int id = 1;
    private String application;
    private int operationMode;
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
