package com.lester.mytaxi_challenge.repository;

import com.lester.mytaxi_challenge.repository.datasource.remote.VehicleRest;
import com.lester.mytaxi_challenge.repository.datasource.remote.VehicleRestInt;

public class VehicleRepository implements VehicleRepositoryInt {

    VehicleRestInt dataSource;

    public VehicleRepository(VehicleRestInt dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void getVehicles(double p1Lat, double p1Lon, double p2Lat, double p2Lon, final VehicleRest.Callback callback) {
        dataSource.getVehicles(p1Lat, p1Lon, p2Lat, p2Lon, callback);
    }
}