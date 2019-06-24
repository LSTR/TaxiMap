package com.lester.mytaxi_challenge.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.lester.mytaxi_challenge.repository.VehicleRepository;
import com.lester.mytaxi_challenge.repository.datasource.remote.VehicleRest;
import com.lester.mytaxi_challenge.repository.datasource.remote.retrofit.ApiService;

public class VehicleViewModelFactory implements ViewModelProvider.Factory{

    @NonNull
    @Override
    public VehicleViewModel create(@NonNull Class modelClass) {
        return new VehicleViewModel(new VehicleRepository(new VehicleRest(new ApiService())));
    }
}