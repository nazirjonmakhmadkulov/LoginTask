package com.nazirjon.logintask.database;

import com.nazirjon.logintask.models.User;

import java.util.List;

import io.reactivex.Flowable;


public interface IUserLocalDataSource  {

    Flowable<User> getUserById(String userId);
    Flowable<List<User>> getAllUsers();
    void insertUser(User... users);

}
