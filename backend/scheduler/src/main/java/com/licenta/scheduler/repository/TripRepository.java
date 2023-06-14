package com.licenta.scheduler.repository;

import com.licenta.scheduler.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByUserId(long userId);

    @Query("FROM Trip t WHERE t.status= 'Waiting' ")
    List<Trip> findAllWaiting();

    List<Trip> findByVehicleId(long vehicleId);
}
