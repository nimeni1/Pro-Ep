package proep.fhict.work.driver;

import okhttp3.ResponseBody;
import proep.fhict.work.driver.model.Car;
import proep.fhict.work.driver.model.Driver;
import proep.fhict.work.driver.model.Fare;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {

    @GET("logIn/{email}/{pass}")
    Call<Driver> logIn(@Query("email") String email, @Query("pass") String pass);

    @GET("askForCarAndPrice/{driverEmail}")
    Call<Car> askForCarAndPrice( @Path("driverEmail") String email);

    @POST("createFare")
    Call<ResponseBody> createFare( @Body Fare fare );

}
