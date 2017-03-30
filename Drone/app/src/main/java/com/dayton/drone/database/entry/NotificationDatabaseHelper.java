package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.bean.NotificationBean;
import com.dayton.drone.model.Notification;

import net.medcorp.library.ble.util.Optional;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by med on 16/5/9.
 */
public class NotificationDatabaseHelper {

    public NotificationDatabaseHelper(Context context) {

    }

    public Optional<Notification> add(Notification object) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        NotificationBean notificationBean = realm.copyToRealm(convertToBean(new NotificationBean(),object));
        realm.commitTransaction();
        return new Optional<>(convertToNormal(new Notification(),notificationBean));
    }


    public boolean update(Notification object) {
        Realm realm = Realm.getDefaultInstance();
        NotificationBean notificationBean = realm.where(NotificationBean.class).equalTo(NotificationBean.fApplication, object.getApplication()).findFirst();
        if(notificationBean==null) {
            return add(object).notEmpty();
        }
        realm.beginTransaction();
        convertToBean(notificationBean,object);
        realm.commitTransaction();
        return true;
    }


    public boolean remove(String application) {
        Realm realm = Realm.getDefaultInstance();
        NotificationBean notificationBean = realm.where(NotificationBean.class).equalTo(NotificationBean.fApplication, application).findFirst();
        if(notificationBean!=null) {
            realm.beginTransaction();
            notificationBean.deleteFromRealm();
            realm.commitTransaction();
            return true;
        }
        return false;
    }

    public List<Notification> get(String application) {
        Realm realm = Realm.getDefaultInstance();
        List<Notification> list = new ArrayList<>();
        NotificationBean notificationBean = realm.where(NotificationBean.class).equalTo(NotificationBean.fApplication, application).findFirst();
        if(notificationBean!=null) {
            list.add(convertToNormal(new Notification(),notificationBean));
        }
        return list;
    }

    public List<Notification> getAll() {
        Realm realm = Realm.getDefaultInstance();
        List<Notification> list = new ArrayList<>();
        List<NotificationBean> all = realm.where(NotificationBean.class).findAll();
        for(NotificationBean notificationBean:all){
            list.add(convertToNormal(new Notification(),notificationBean));
        }
        return list;
    }

    private Notification convertToNormal(Notification notification ,NotificationBean notificationBean) {
        notification.setApplication(notificationBean.getApplication());
        notification.setContactsList(notificationBean.getContactsList());
        notification.setOperationMode(notificationBean.getOperationMode());
        return notification;
    }

    private NotificationBean convertToBean(NotificationBean notificationBean,Notification notification) {
        notificationBean.setApplication(notification.getApplication());
        notificationBean.setContactsList(notification.getContactsList());
        notificationBean.setOperationMode(notification.getOperationMode());
        return notificationBean;
    }

}
