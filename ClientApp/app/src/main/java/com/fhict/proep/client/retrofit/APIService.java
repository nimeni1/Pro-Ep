package com.fhict.proep.client.retrofit;

import com.fhict.proep.client.models.Client;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("logIn/{email}/{pass}")
    Call<Client> logIn(@Query("email") String email, @Query("pass") String pass);

    @POST("register")
    Call<ResponseBody> register(@Body Client client);


}
