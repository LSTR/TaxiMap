package com.lester.mytaxi_challenge.repository.datasource.remote;

public interface VehicleRestInt {
    void getVehicles(double p1Lat, double p1Lon, double p2Lat, double p2Lon, final VehicleRest.Callback callback);
}