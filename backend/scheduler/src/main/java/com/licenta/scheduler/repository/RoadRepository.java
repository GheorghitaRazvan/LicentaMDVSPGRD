package com.licenta.scheduler.repository;

import com.licenta.scheduler.model.Location;
import com.licenta.scheduler.model.Road;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadRepository extends JpaRepository<Road, Long> {
    List<Road> findAllByStartingLocationOrFinishingLocation(Location startingL, Location finishingL);

    Road findByStartingLocationAndFinishingLocation(Location startingL, Location finishingL);
}
