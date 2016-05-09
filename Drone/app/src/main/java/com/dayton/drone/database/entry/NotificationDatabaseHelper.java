package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.NotificationBean;
import com.dayton.drone.modle.Notification;

import net.medcorp.library.ble.util.Optional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by med on 16/5/9.
 */
public class NotificationDatabaseHelper {

    private DatabaseHelper databaseHelper;

    public NotificationDatabaseHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelperInstance(context);
        }
    }


    public Optional<Notification> add(Notification object) {
        Optional<Notification> optional = new Optional<>();
        try {
            int res = databaseHelper.getNotificationBean().create(convertToBean(object));
            if (res > 0) {
                optional.set(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optional;
    }


    public boolean update(Notification object) {
        int result = -1;
        try {
            List<NotificationBean> notificationList = databaseHelper.getNotificationBean().queryBuilder()
                    .where().eq(NotificationBean.fApplication, object.getApplication()).query();
            if (notificationList.isEmpty()) {
                return add(object) != null;
            }
            NotificationBean notificationBean = convertToBean(object);
            notificationBean.setId(notificationList.get(0).getId());
            result = databaseHelper.getNotificationBean().update(notificationBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result >= 0;
    }


    public boolean remove(String application) {
        try {
            List<NotificationBean> notificationList = databaseHelper.getNotificationBean().queryBuilder()
                    .where().eq(NotificationBean.fApplication, application).query();
            if (!notificationList.isEmpty()) {
                return databaseHelper.getNotificationBean().delete(notificationList) >= 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Notification> get(String application) {
        List<Notification> list = new ArrayList<>();

        try {
            List<NotificationBean> notificationList = databaseHelper.getNotificationBean().queryBuilder()
                    .where().eq(NotificationBean.fApplication, application).query();

            if(!notificationList.isEmpty()) {
                list.add(convertToNormal(notificationList.get(0)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Notification> getAll() {
        List<Notification> list = new ArrayList<>();
        try {
            List<NotificationBean> notificationList = databaseHelper.getNotificationBean().queryBuilder().query();

            for(NotificationBean notificationBean:notificationList)
            {
                list.add(convertToNormal(notificationBean));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Notification convertToNormal(NotificationBean notificationBean) {
        Notification notification = new Notification();
        notification.setApplication(notificationBean.getApplication());
        notification.setContactsList(notificationBean.getContactsList());
        notification.setOperationMode(notificationBean.getOperationMode());
        return notification;
    }

    private NotificationBean convertToBean(Notification notification) {
        NotificationBean notificationBean = new NotificationBean();
        notificationBean.setApplication(notification.getApplication());
        notificationBean.setContactsList(notification.getContactsList());
        notificationBean.setOperationMode(notification.getOperationMode());
        return notificationBean;
    }
}
