package com.lester.mytaxi_challenge.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lester.mytaxi_challenge.repository.VehicleRepositoryInt;
import com.lester.mytaxi_challenge.repository.datasource.remote.VehicleRest;
import com.lester.mytaxi_challenge.repository.model.VehicleE;

import java.util.ArrayList;

public class VehicleViewModel extends ViewModel {
    private VehicleRepositoryInt vehicleRepository;
    public MutableLiveData<ArrayList<VehicleE>> dataListLive = new MutableLiveData<>();
    public MutableLiveData<String> messageLive = new MutableLiveData<>();
    public MutableLiveData<Boolean> showLoadingLive = new MutableLiveData<>();

    public void provide(VehicleRepositoryInt vehicleRepository){
        this.vehicleRepository = vehicleRepository;
    }

    public void loadVehicles(double p1Lat, double p1Lon, double p2Lat, double p2Lon){
        showLoadingLive.postValue(true);
        vehicleRepository.getVehicles(p1Lat, p1Lon, p2Lat, p2Lon, new VehicleRest.Callback() {
            @Override
            public void onSuccess(ArrayList<VehicleE> data) {
                showLoadingLive.postValue(false);
                dataListLive.postValue(data);
            }
            @Override
            public void onError(String error) {
                showLoadingLive.postValue(false);
                messageLive.postValue(error);
            }
        });
    }
}