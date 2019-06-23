package com.lester.mytaxi_challenge.repository;

import com.lester.mytaxi_challenge.repository.datasource.remote.VehicleRest;

public interface VehicleRepositoryInt {
    void getVehicles(double p1Lat, double p1Lon, double p2Lat, double p2Lon, final VehicleRest.Callback callback);
}