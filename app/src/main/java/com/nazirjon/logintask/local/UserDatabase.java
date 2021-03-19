package com.nazirjon.logintask.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.nazirjon.logintask.models.User;

@Database(entities = User.class, version = UserDatabase.DATABASE_VERSION)
public abstract class UserDatabase extends RoomDatabase {

    public static final int DATABASE_VERSION = 1;

    public abstract UserDAO userDAO();

    private static volatile UserDatabase mInstance;

    public static UserDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (UserDatabase.class) {
                if (mInstance == null) {
                    mInstance = Room.databaseBuilder(context,
                            UserDatabase.class, "user.db").build();
                }
            }
        }
        return mInstance;
    }

}



