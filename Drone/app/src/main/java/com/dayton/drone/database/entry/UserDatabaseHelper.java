package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.UserBean;
import com.dayton.drone.modle.User;

import net.medcorp.library.ble.util.Optional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by boy on 2016/4/12.
 */
public class UserDatabaseHelper implements iEntryDatabaseHelper<User> {

    private DatabaseHelper databaseHelper;

    private UserDatabaseHelper(Context context) {
        if (databaseHelper == null) {
            databaseHelper = DatabaseHelper.getHelperInstance(context);
        }
    }

    @Override
    public Optional<User> add(User object) {
        Optional<User> userOptional = new Optional<>();
        try {
            int res = databaseHelper.getUserBean().create(convertToBean(object));
            if (res > 0) {
                userOptional.set(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userOptional;
    }


    @Override
    public boolean update(User object) {
        int result = -1;
        try {
            List<UserBean> userList = databaseHelper.getUserBean().queryBuilder()
                    .where().eq(UserBean.fNevoUserID, object.getDroneUserID()).query();
            if (userList.isEmpty()) {
                return add(object) != null;
            }
            UserBean userBean = convertToBean(object);
            userBean.setID(userList.get(0).getID());
            result = userList.get(0).getID();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result >= 0;
    }

    @Override
    public boolean remove(String userId, Date date) {
        try {
            List<UserBean> userList = databaseHelper.getUserBean().queryBuilder()
                    .where().eq(UserBean.fNevoUserID, userId).query();
            if (userList.isEmpty()) {
                return databaseHelper.getUserBean().delete(userList) >= 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //single find
    @Override
    public List<Optional<User>> get(String userId) {

        List<Optional<User>> list = new ArrayList<>();
        try {
            List<UserBean> userList = databaseHelper.getUserBean()
                    .queryBuilder().where().eq(UserBean.fNevoUserID, userId).query();
            for (UserBean userBean : userList) {
                Optional<User> optional = new Optional<>();
                optional.set(convertToNormal(userBean));
                list.add(optional);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    //find table
    @Override
    public Optional<User> get(String userId, Date date) {
        List<Optional<User>> userList = get(userId);
        return userList.isEmpty() ? new Optional<User>() : userList.get(0);
    }

    // find all
    @Override
    public List<Optional<User>> getAll(String userId) {
        List<Optional<User>> userList = get(userId);
        return userList;
    }

    // convert list
    @Override
    public List<User> convertToNormalList(List<Optional<User>> optionals) {
        List<User> list = new ArrayList<>(3);
        for (Optional<User> userOptional : optionals) {
            if (userOptional.notEmpty())
                list.add(userOptional.get());
        }
        return list;
    }

    private User convertToNormal(UserBean userDAO) {
        User user = new User(userDAO.getCreatedDate());
        user.setId(userDAO.getID());
        user.setAge(userDAO.getAge());
        user.setHeight(userDAO.getHeight());
        user.setBirthday(userDAO.getBirthday());
        user.setWeight(userDAO.getWeight());
        user.setRemarks(userDAO.getRemarks());
        user.setFirstName(userDAO.getFirstName());
        user.setLastName(userDAO.getLastName());
        user.setSex(userDAO.getSex());
        user.setDroneUserEmail(userDAO.getUserEmail());
        user.setDroneUserID(userDAO.getUserID());
        user.setDroneUserToken(userDAO.getUserToken());
        user.setValidicUserID(userDAO.getValidicUserID());
        user.setValidicUserToken(userDAO.getValidicUserToken());
        user.setIsLogin(userDAO.isUserIsLogin());
        user.setIsConnectValidic(userDAO.isValidicUserIsConnected());
        return user;
    }

    public UserBean convertToBean(User user) {
        UserBean userDAO = new UserBean();
        userDAO.setCreatedDate(user.getCreatedDate());
        userDAO.setHeight(user.getHeight());
        userDAO.setAge(user.getAge());
        userDAO.setBirthday(user.getBirthday());
        userDAO.setWeight(user.getWeight());
        userDAO.setRemarks(user.getRemarks());
        userDAO.setFirstName(user.getFirstName());
        userDAO.setLastName(user.getLastName());
        userDAO.setSex(user.getSex());
        userDAO.setUserEmail(user.getDroneUserEmail());
        userDAO.setUserID(user.getDroneUserID());
        userDAO.setUserToken(user.getDroneUserToken());
        userDAO.setValidicUserID(user.getValidicUserID());
        userDAO.setValidicUserToken(user.getValidicUserToken());
        userDAO.setUserIsLogin(user.isLogin());
        userDAO.setValidicUserIsConnected(user.isConnectValidic());
        return userDAO;
    }


}
