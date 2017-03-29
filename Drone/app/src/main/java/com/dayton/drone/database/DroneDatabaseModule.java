package com.dayton.drone.database;

import com.dayton.drone.model.Notification;
import com.dayton.drone.model.Steps;
import com.dayton.drone.model.User;
import com.dayton.drone.model.Watches;

import io.realm.annotations.RealmModule;

/**
 * Created by med on 17/3/28.
 */
@RealmModule(classes = {Notification.class, Steps.class, User.class, Watches.class})
public class DroneDatabaseModule {
}
