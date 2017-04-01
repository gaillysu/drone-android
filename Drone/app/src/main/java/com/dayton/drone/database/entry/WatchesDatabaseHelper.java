package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.bean.WatchesBean;
import com.dayton.drone.model.Watches;

import net.medcorp.library.ble.util.Optional;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by med on 16/5/19.
 */
public class WatchesDatabaseHelper {

    public WatchesDatabaseHelper(Context context) {

    }

    public Optional<Watches> add(final Watches object) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final WatchesBean watchesBean = realm.copyToRealm(convertToBean(new WatchesBean(),object));
        realm.commitTransaction();
        return new Optional<>(convertToNormal(new Watches(),watchesBean));
    }

    public boolean update(Watches object) {
        Realm realm = Realm.getDefaultInstance();
        WatchesBean watchesBean = realm.where(WatchesBean.class).equalTo(WatchesBean.fMacAddress, object.getMacAddress()).findFirst();
        if(watchesBean==null){
            return add(object).notEmpty();
        }
        realm.beginTransaction();
        convertToBean(watchesBean,object);
        realm.commitTransaction();
        return true;
    }

    public boolean remove(Watches object) {
        Realm realm = Realm.getDefaultInstance();
        WatchesBean watchesBean = realm.where(WatchesBean.class).equalTo(WatchesBean.fMacAddress, object.getMacAddress()).equalTo(WatchesBean.fUserID, object.getUserID()).findFirst();
        if(watchesBean!=null){
            realm.beginTransaction();
            watchesBean.deleteFromRealm();
            realm.commitTransaction();
            return true;
        }
        return false;
    }

    public List<Watches> getAll(String userId) {
        Realm realm = Realm.getDefaultInstance();
        List<Watches> list = new ArrayList<>();
        List<WatchesBean> all = realm.where(WatchesBean.class).equalTo(WatchesBean.fUserID, userId).findAll();
        for(WatchesBean watchesBean:all){
            list.add(convertToNormal(new Watches(),watchesBean));
        }
        return list;
    }


    private Watches convertToNormal(Watches watches, WatchesBean watchesBean)
    {
        watches.setWatchID(watchesBean.getWatchID());
        watches.setUserID(watchesBean.getUserID());
        watches.setSerialNumber(watchesBean.getSerialNumber());
        watches.setMacAddress(watchesBean.getMacAddress());
        watches.setModelName(watchesBean.getModelName());
        watches.setFirmwareVersion(watchesBean.getFirmwareVersion());
        return watches;
    }

    private WatchesBean convertToBean(WatchesBean watchesBean,Watches watches)
    {
        watchesBean.setWatchID(watches.getWatchID());
        watchesBean.setUserID(watches.getUserID());
        watchesBean.setSerialNumber(watches.getSerialNumber());
        watchesBean.setMacAddress(watches.getMacAddress());
        watchesBean.setModelName(watches.getModelName());
        watchesBean.setFirmwareVersion(watches.getFirmwareVersion());
        return watchesBean;
    }

}
