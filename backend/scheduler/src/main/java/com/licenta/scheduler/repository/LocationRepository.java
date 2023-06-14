package com.licenta.scheduler.repository;

import com.licenta.scheduler.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("FROM Location l WHERE l.type= 'Depot' ")
    List<Location> findAllDepots();

    @Query("FROM Location l WHERE l.type= 'Location' ")
    List<Location> findAllNormalLocations();
}
