package com.licenta.scheduler.repository;

import com.licenta.scheduler.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByEmail(String email);

    Driver findByVehicleId(long vehicleId);
}
