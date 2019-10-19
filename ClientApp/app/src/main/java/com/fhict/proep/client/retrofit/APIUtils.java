package com.fhict.proep.client.retrofit;

public class APIUtils {

    private APIUtils() {}

    private static final String BASE_URL = "http://192.168.43.142:8080/client/rest/client/";
//145.93.62.249
    public static APIService getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
