package com.dayton.drone.database.bean;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by med on 17/3/30.
 */

public class NotificationBean extends RealmObject {
    /**
     * field name and initialize value, Primary field
     */
    @Ignore
    public static final String fID = "id";
    private int id = 1;

    @Ignore
    public static final String fApplication = "application";
    private String application;

    /**
     * black/white list mode
     */
    @Ignore
    public static final String fOperationMode = "operationMode";
    private int operationMode;


    /**
     * JSONArray string,eg:{"contacts":[{"name":"Tom","number":"10086"},{"name":"John","number":"10000;10001"},{},...]}
     */
    @Ignore
    public static final String fContactsList = "contactsList";
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
