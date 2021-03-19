package com.nazirjon.logintask.models;

import androidx.room.ColumnInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.NonNull;

public class ResponseApi {

    @NonNull
    @ColumnInfo(name = "_id")
    private String id;
    @ColumnInfo(name = "token")
    private String token;
    @ColumnInfo(name = "access_token")
    private String accessToken;
    @ColumnInfo(name = "refresh_token")
    private String refreshToken;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

