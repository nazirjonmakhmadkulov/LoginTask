package com.nazirjon.logintask.local;
import com.nazirjon.logintask.database.IUserLocalDataSource;
import com.nazirjon.logintask.models.User;

import java.util.List;

import io.reactivex.Flowable;

public class UserDataSource implements IUserLocalDataSource {

    private UserDAO userDAO;
    private static UserDataSource mInstance;

    public UserDataSource(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public static UserDataSource getInstance(UserDAO userDAO){
        if (mInstance == null){
            mInstance = new UserDataSource(userDAO);
        }
        return mInstance;
    }

    @Override
    public Flowable<User> getUserById(String userId) {
        return userDAO.getUserById(userId);
    }

    @Override
    public Flowable<List<User>> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public void insertUser(User... user) {
        userDAO.insertUser(user);
    }
}
