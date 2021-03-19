package com.nazirjon.logintask.database;


import com.nazirjon.logintask.models.User;

import java.util.List;

import io.reactivex.Flowable;

public class UserRespository implements IUserLocalDataSource{

    private IUserLocalDataSource mLocalDataSource ;
    private volatile static UserRespository mInstance;

    public UserRespository(IUserLocalDataSource mLocalDataSource) {
        this.mLocalDataSource = mLocalDataSource;
    }

    public static UserRespository getInstance(IUserLocalDataSource mLocalDataSource){
        if (mInstance == null){
            mInstance = new UserRespository(mLocalDataSource);
        }
        return mInstance;
    }

    @Override
    public Flowable<User> getUserById(String userId) {
        return mLocalDataSource.getUserById(userId);
    }

    @Override
    public Flowable<List<User>> getAllUsers() {
        return mLocalDataSource.getAllUsers();
    }

    @Override
    public void insertUser(User... users) {
        mLocalDataSource.insertUser(users);
    }

}