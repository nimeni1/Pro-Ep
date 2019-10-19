package proep.fhict.work.driver;

public class APIUtils {

    private APIUtils() {}

    private static final String BASE_URL = "http://192.168.43.142:8080/driver/rest/driver/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
