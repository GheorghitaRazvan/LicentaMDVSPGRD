package com.licenta.scheduler.model;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private Location depot;

    private Vehicle vehicle;

    private List<Trip> trips = new ArrayList<>();

    public Location getDepot() {
        return depot;
    }

    public void setDepot(Location depot) {
        this.depot = depot;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void addTrip(Trip trip) {
        this.trips.add(trip);
    }
}
