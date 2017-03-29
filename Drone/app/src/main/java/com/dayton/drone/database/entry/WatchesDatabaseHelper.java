package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.model.Watches;

import net.medcorp.library.ble.util.Optional;

import java.util.List;

import io.realm.Realm;

/**
 * Created by med on 16/5/19.
 */
public class WatchesDatabaseHelper {
    private Realm realm;

    public WatchesDatabaseHelper(Context context) {
           realm = Realm.getDefaultInstance();
    }

    public Optional<Watches> add(final Watches object) {
        realm.beginTransaction();
        final Watches watches = realm.copyToRealm(object);
        realm.commitTransaction();
        return new Optional<>(watches);
    }

    public boolean update(Watches object) {
        Watches watches = realm.where(Watches.class).equalTo(Watches.fMacAddress, object.getMacAddress()).findFirst();
        if(watches==null){
            return add(object).notEmpty();
        }
        realm.beginTransaction();
        copyToRealm(watches,object);
        realm.commitTransaction();
        return true;
    }

    public boolean remove(Watches object) {
        Watches watches = realm.where(Watches.class).equalTo(Watches.fMacAddress, object.getMacAddress()).equalTo(Watches.fUserID, object.getUserID()).findFirst();
        if(watches!=null){
            realm.beginTransaction();
            watches.deleteFromRealm();
            realm.commitTransaction();
            return true;
        }
        return false;
    }

    public List<Watches> getAll(String userId) {
        return realm.where(Watches.class).equalTo(Watches.fUserID, userId).findAll();
    }

    private void copyToRealm(Watches watchesRealm, Watches object){
        watchesRealm.setWatchID(object.getWatchID());
        watchesRealm.setUserID(object.getUserID());
        watchesRealm.setSerialNumber(object.getSerialNumber());
        watchesRealm.setMacAddress(object.getMacAddress());
        watchesRealm.setModelName(object.getModelName());
        watchesRealm.setFirmwareVersion(object.getFirmwareVersion());
    }
}
