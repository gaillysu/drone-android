package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.model.User;

import net.medcorp.library.ble.util.Optional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by boy on 2016/4/12.
 */
public class UserDatabaseHelper implements iEntryDatabaseHelper<User> {

    private Realm realm;

    public UserDatabaseHelper(Context context) {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public Optional<User> add(User object) {
        realm.beginTransaction();
        User user = realm.copyToRealm(object);
        realm.commitTransaction();
        return new Optional<>(user);
    }


    @Override
    public boolean update(User object) {
        User user = realm.where(User.class).equalTo(User.fUserID, object.getUserID()).findFirst();
        if(user == null){
            return add(object).notEmpty();
        }

        realm.beginTransaction();
        copyToRealm(user,object);
        realm.commitTransaction();
        return true;
    }

    @Override
    public boolean remove(String userId ,Date date) {
        User user = realm.where(User.class).equalTo(User.fUserID, userId).findFirst();
        if(user != null) {
            realm.beginTransaction();
            user.deleteFromRealm();
            realm.commitTransaction();
            return true;
        }
        return false;
    }

    public boolean removeAll(){
        RealmResults<User> users = realm.where(User.class).findAll();
        realm.beginTransaction();
        boolean result = users.deleteAllFromRealm();
        realm.commitTransaction();
        return result;
    }

    //single find
    @Override
    public List<Optional<User>> get(String userId) {
        List<Optional<User>> list = new ArrayList<>();
        User user = realm.where(User.class).equalTo(User.fUserID, userId).findFirst();
        if(user != null) {
            list.add(new Optional<>(user));
        }
        return list;
    }

    //find table
    @Override
    public Optional<User> get(String userId, Date date) {
        User user = realm.where(User.class).equalTo(User.fUserID, userId).findFirst();
        if(user != null) {
            return new Optional<>(user);
        }
        return new Optional<>();
    }

    // find all
    @Override
    public List<Optional<User>> getAll(String userId) {
        return get(userId);
    }

    // convert list
    @Override
    public List<User> convertToNormalList(List<Optional<User>> optionals) {
        List<User> list = new ArrayList<>();
        for (Optional<User> userOptional : optionals) {
            if (userOptional.notEmpty())
                list.add(userOptional.get());
        }
        return list;
    }

    public Optional<User> getLoginUser()    {
        User user = realm.where(User.class).equalTo(User.fUserIsLogin, true).findFirst();
        if(user !=null) {
            return new Optional<>(user);
        }
        return new Optional<>();
    }

    private void copyToRealm(User userRealm, User object) {
        userRealm.setBirthday(object.getBirthday());
        userRealm.setHeight(object.getHeight());
        userRealm.setWeight(object.getWeight());
        userRealm.setFirstName(object.getFirstName());
        userRealm.setLastName(object.getLastName());
        userRealm.setGender(object.getGender());
        userRealm.setStrideLength(object.getStrideLength());
        userRealm.setUserEmail(object.getUserEmail());
        userRealm.setUserPassword(object.getUserPassword());
        userRealm.setUserID(object.getUserID());
        userRealm.setUserIsLogin(object.isUserIsLogin());
    }

}
