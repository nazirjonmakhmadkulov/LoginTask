package com.nazirjon.logintask.network;

import android.os.Build;

import java.io.IOException;
import java.util.Locale;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {

    private String authToken;
    public AuthenticationInterceptor(String token) {
        this.authToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder builder = original.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Authorization",  "Bearer " + authToken);
        Request request = builder.build();
        return chain.proceed(request);
    }
}
