package com.lester.mytaxi_challenge.repository.datasource.remote.retrofit;

import com.lester.mytaxi_challenge.repository.model.VehicleDataResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public class ApiService extends ApiClient{

    private UserSessionInterface apiService;

    public ApiService() {
        apiService = retrofit.create(UserSessionInterface.class);
    }

    public UserSessionInterface getApi() {
        return apiService;
    }

    public interface UserSessionInterface{
        @GET("/")
        Observable<VehicleDataResponse> getVehicleData(@Query("p1Lat") double p1Lat, @Query("p1Lon") double p1Lon,
                                                       @Query("p2Lat") double p2Lat, @Query("p2Lon") double p2Lon);
    }
}