package com.nazirjon.logintask.local;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.nazirjon.logintask.models.User;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface UserDAO {

    @Query("SELECT * FROM user WHERE _id=:userId")
    Flowable<User> getUserById(String userId);

    @Query("SELECT * FROM user")
    Flowable<List<User>> getAllUsers();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User... user);

}