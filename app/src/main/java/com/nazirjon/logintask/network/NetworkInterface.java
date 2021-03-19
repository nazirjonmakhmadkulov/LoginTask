package com.nazirjon.logintask.network;

import com.nazirjon.logintask.models.Create;
import com.nazirjon.logintask.models.ResponseApi;
import com.nazirjon.logintask.models.Login;
import com.nazirjon.logintask.models.User;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface NetworkInterface {

    @GET("/user")
    Call<User> checkToken();

    @POST("/user")
    Call<ResponseApi> createAccount(@Body Create create);

    @POST("/user/login")
    Call<ResponseApi> login(@Body Login login);

}
