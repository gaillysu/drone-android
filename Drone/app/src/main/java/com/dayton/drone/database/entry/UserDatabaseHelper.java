package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.bean.UserBean;
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

    public UserDatabaseHelper(Context context) {

    }

    @Override
    public Optional<User> add(User object) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        UserBean userBean = realm.copyToRealm(convertToBean(new UserBean(),object));
        realm.commitTransaction();
        return new Optional<>(convertToNormal(new User(),userBean));
    }


    @Override
    public boolean update(User object) {
        Realm realm = Realm.getDefaultInstance();
        UserBean userBean = realm.where(UserBean.class).equalTo(UserBean.fUserID, object.getUserID()).findFirst();
        if(userBean == null){
            return add(object).notEmpty();
        }

        realm.beginTransaction();
        convertToBean(userBean,object);
        realm.commitTransaction();
        return true;
    }

    @Override
    public boolean remove(String userId ,Date date) {
        Realm realm = Realm.getDefaultInstance();
        UserBean userBean = realm.where(UserBean.class).equalTo(UserBean.fUserID, userId).findFirst();
        if(userBean != null) {
            realm.beginTransaction();
            userBean.deleteFromRealm();
            realm.commitTransaction();
            return true;
        }
        return false;
    }

    public boolean removeAll(){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<UserBean> users = realm.where(UserBean.class).findAll();
        realm.beginTransaction();
        boolean result = users.deleteAllFromRealm();
        realm.commitTransaction();
        return result;
    }

    //single find
    @Override
    public List<Optional<User>> get(String userId) {
        Realm realm = Realm.getDefaultInstance();
        List<Optional<User>> list = new ArrayList<>();
        UserBean userBean = realm.where(UserBean.class).equalTo(UserBean.fUserID, userId).findFirst();
        if(userBean != null) {
            list.add(new Optional<>(convertToNormal(new User(),userBean)));
        }
        return list;
    }

    //find table
    @Override
    public Optional<User> get(String userId, Date date) {
        Realm realm = Realm.getDefaultInstance();
        UserBean userBean = realm.where(UserBean.class).equalTo(UserBean.fUserID, userId).findFirst();
        if(userBean != null) {
            return new Optional<>(convertToNormal(new User(),userBean));
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

    public Optional<User> getLoginUser()
    {
        Realm realm = Realm.getDefaultInstance();
        UserBean userBean = realm.where(UserBean.class).equalTo(UserBean.fUserIsLogin, true).findFirst();
        if(userBean !=null) {
            return new Optional<>(convertToNormal(new User(),userBean));
        }
        return new Optional<>();
    }

    private User convertToNormal(User user,UserBean userDAO) {
        user.setBirthday(userDAO.getBirthday());
        user.setHeight(userDAO.getHeight());
        user.setWeight(userDAO.getWeight());
        user.setFirstName(userDAO.getFirstName());
        user.setLastName(userDAO.getLastName());
        user.setGender(userDAO.getGender());
        user.setStrideLength(userDAO.getStrideLength());
        user.setUserEmail(userDAO.getUserEmail());
        user.setUserPassword(userDAO.getUserPassword());
        user.setUserID(userDAO.getUserID());
        user.setUserIsLogin(userDAO.isUserIsLogin());
        return user;
    }

    public UserBean convertToBean(UserBean userDAO,User user) {
        userDAO.setHeight(user.getHeight());
        userDAO.setBirthday(user.getBirthday());
        userDAO.setWeight(user.getWeight());
        userDAO.setFirstName(user.getFirstName());
        userDAO.setLastName(user.getLastName());
        userDAO.setGender(user.getGender());
        userDAO.setStrideLength(user.getStrideLength());
        userDAO.setUserEmail(user.getUserEmail());
        userDAO.setUserPassword(user.getUserPassword());
        userDAO.setUserID(user.getUserID());
        userDAO.setUserIsLogin(user.isUserIsLogin());
        return userDAO;
    }


}
