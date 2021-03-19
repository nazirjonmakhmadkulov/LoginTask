package com.nazirjon.logintask.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;

public class Settings {
    private static String TAG = Settings.class.getSimpleName();

    static SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoginTask";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";
    public static final String ID = "id";

    public Settings(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public boolean deleteAllSharedPrefs() {
        return sharedPreferences.edit().clear().commit();
    }

    public String getLogin() {//получение login
        return sharedPreferences.getString(LOGIN, "");
    }

    public void saveLogin(String login) {//сохранение login
        editor.putString(LOGIN, login);
        editor.commit();
    }

    public String getPassword() {//получение password
        return sharedPreferences.getString(PASSWORD, "");
    }

    public void savePassword(String password) {//сохранение password
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    public String getToken() {//получение token
        return sharedPreferences.getString(TOKEN, "");
    }

    public void saveToken(String token) {//сохранение token
        editor.putString(TOKEN, token);
        editor.commit();
    }

    public String getId() {//получение id
        return sharedPreferences.getString(ID, "");
    }

    public void saveId(String id) {//сохранение id
        editor.putString(ID, id);
        editor.commit();
    }

    public void deleteToken() {//delete
        editor.clear();
        editor.commit();
    }
}