package com.lester.mytaxi_challenge.repository.model;

public class VehicleE {
    long id;
    String fleetType;
    double heading;
    Coordinate coordinate;

    public class Coordinate {
        double latitude;
        double longitude;

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }
    }

    public long getId() {
        return id;
    }

    public String getFleetType() {
        return fleetType;
    }

    public boolean isPooling() {
        return fleetType.equals("POOLING");
    }

    public double getHeading() {
        return heading;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }
}