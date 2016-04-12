package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelperBase;
import com.dayton.drone.database.bean.UserBean;
import com.dayton.drone.modle.User;

import net.medcorp.library.ble.util.Optional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by boy on 2016/4/12.
 */
public class UserDatabaseHelper implements iEntryDatabaseHelper<User> {

    private DatabaseHelperBase databaseHelper;

    private UserDatabaseHelper(Context context) {
        databaseHelper = DatabaseHelperBase.getHelperInstance(context);
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
        return false;
    }

    @Override
    public boolean remove(String userId, Date date) {
        return false;
    }

    @Override
    public List<Optional<User>> get(String userId) {
        return null;
    }

    @Override
    public Optional<User> get(String userId, Date date) {
        return null;
    }

    @Override
    public List<Optional<User>> getAll(String userId) {
        return null;
    }

    @Override
    public List<User> convertToNormalList(List<Optional<User>> optionals) {
        return null;
    }

    private User convertToNormal(UserBean userDAO){
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
        user.setDroneUserEmail(userDAO.getDroenUserEmail());
        user.setDroneUserID(userDAO.getDroneUserId());
        user.setDroneUserToken(userDAO.getDroneUserToken());
        user.setValidicUserID(userDAO.getValidicUserID());
        user.setValidicUserToken(userDAO.getValidicUserToken());
        user.setIsLogin(userDAO.isDroneUserIsLogin());
        user.setIsConnectValidic(userDAO.isValidicUserIsConnected());
        return user;
    }

    public UserBean convertToBean(User user){
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
        userDAO.setDroenUserEmail(user.getDroneUserEmail());
        userDAO.setDroneUserId(user.getDroneUserID());
        userDAO.setDroneUserToken(user.getDroneUserToken());
        userDAO.setValidicUserID(user.getValidicUserID());
        userDAO.setValidicUserToken(user.getValidicUserToken());
        userDAO.setDroneUserIsLogin(user.isLogin());
        userDAO.setValidicUserIsConnected(user.isConnectValidic());
        return userDAO;
    }


}
