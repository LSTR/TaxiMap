package com.lester.mytaxi_challenge.repository.datasource.remote;

import com.lester.mytaxi_challenge.repository.datasource.remote.retrofit.ApiService;
import com.lester.mytaxi_challenge.repository.model.VehicleDataResponse;
import com.lester.mytaxi_challenge.repository.model.VehicleE;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class VehicleRest implements VehicleRestInt {
    private ApiService apiService;

    public VehicleRest(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void getVehicles(double p1Lat, double p1Lon, double p2Lat, double p2Lon, final Callback callback) {
        Observable<VehicleDataResponse> observable = apiService.getApi().getVehicleData(p1Lat, p1Lon, p2Lat, p2Lon);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Action1<VehicleDataResponse>() {
                               @Override
                               public void call(VehicleDataResponse data) {
                                   callback.onSuccess(data.getPoiList());
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                callback.onError(throwable.getMessage());
                            }
                        });
    }

    public interface Callback{
        void onSuccess(ArrayList<VehicleE> data);
        void onError(String error);
    }
}