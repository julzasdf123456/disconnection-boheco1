package com.lopez.julz.disconnection.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    public Retrofit retrofit;

    public RetrofitBuilder(String ip) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600,TimeUnit.SECONDS).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.baseUrl(ip)).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public RetrofitBuilder() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.baseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }
}
