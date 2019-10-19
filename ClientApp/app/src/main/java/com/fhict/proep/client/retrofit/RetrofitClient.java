package com.fhict.proep.client.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(70, TimeUnit.SECONDS)
    .writeTimeout(70, TimeUnit.SECONDS)
    .readTimeout(70, TimeUnit.SECONDS)
    .build();

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        }
        return retrofit;
    }
}
