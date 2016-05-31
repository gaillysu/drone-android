package com.dayton.drone.database.entry;

import android.content.Context;

import com.dayton.drone.database.DatabaseHelper;
import com.dayton.drone.database.bean.UserBean;
import com.dayton.drone.model.User;

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

    public UserDatabaseHelper(Context context) {
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
                    .where().eq(UserBean.fUserID, object.getUserID()).query();
            if (userList.isEmpty()) {
                return add(object) != null;
            }
            UserBean userBean = convertToBean(object);
            userBean.setId(userList.get(0).getId());
            result = databaseHelper.getUserBean().update(userBean);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result >= 0;
    }

    @Override
    public boolean remove(String userId ,Date date) {
        try {
            List<UserBean> userList = databaseHelper.getUserBean().queryBuilder()
                    .where().eq(UserBean.fUserID, userId).query();
            if (!userList.isEmpty()) {
                return databaseHelper.getUserBean().delete(userList) >= 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean removeAll(){
        try {
            List<UserBean> userBeanList = databaseHelper.getUserBean().queryForAll();
            return databaseHelper.getUserBean().delete(userBeanList)>=0;

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
                    .queryBuilder().where().eq(UserBean.fUserID, userId).query();
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
        List<User> list = new ArrayList<>();
        for (Optional<User> userOptional : optionals) {
            if (userOptional.notEmpty())
                list.add(userOptional.get());
        }
        return list;
    }

    public Optional<User> getLoginUser()
    {
        Optional<User> optionalUser = new Optional<>();
        try {
            List<UserBean> userList = databaseHelper.getUserBean()
                    .queryBuilder().where().eq(UserBean.fUserIsLogin, true).query();
            for (UserBean userBean : userList) {
                optionalUser.set(convertToNormal(userBean));
                return optionalUser;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return optionalUser;
    }

    private User convertToNormal(UserBean userDAO) {
        User user = new User();
        user.setAge(userDAO.getAge());
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

    public UserBean convertToBean(User user) {
        UserBean userDAO = new UserBean();
        userDAO.setHeight(user.getHeight());
        userDAO.setAge(user.getAge());
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
