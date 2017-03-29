package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.model.Notification;

import net.medcorp.library.ble.util.Optional;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by med on 16/5/9.
 */
public class NotificationDatabaseHelper {

    private Realm realm;

    public NotificationDatabaseHelper(Context context) {
        realm = Realm.getDefaultInstance();
    }

    public Optional<Notification> add(Notification object) {
        realm.beginTransaction();
        Notification notification = realm.copyToRealm(object);
        realm.commitTransaction();
        return new Optional<>(notification);
    }


    public boolean update(Notification object) {
        Notification notification = realm.where(Notification.class).equalTo(Notification.fApplication, object.getApplication()).findFirst();
        if(notification==null) {
            return add(object).notEmpty();
        }
        realm.beginTransaction();
        copyToRealm(notification,object);
        realm.commitTransaction();
        return true;
    }


    public boolean remove(String application) {
        Notification notification = realm.where(Notification.class).equalTo(Notification.fApplication, application).findFirst();
        if(notification!=null) {
            realm.beginTransaction();
            notification.deleteFromRealm();
            realm.commitTransaction();
            return true;
        }
        return false;
    }

    public List<Notification> get(String application) {
        List<Notification> list = new ArrayList<>();
        Notification notification = realm.where(Notification.class).equalTo(Notification.fApplication, application).findFirst();
        if(notification!=null) {
            list.add(notification);
        }
        return list;
    }

    public List<Notification> getAll() {
        return realm.where(Notification.class).findAll();
    }

    private void copyToRealm(Notification notificationRealm,Notification object) {
        notificationRealm.setApplication(object.getApplication());
        notificationRealm.setContactsList(object.getContactsList());
        notificationRealm.setOperationMode(object.getOperationMode());
    }

}
