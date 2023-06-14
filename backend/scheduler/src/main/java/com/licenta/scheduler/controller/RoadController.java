package com.licenta.scheduler.controller;

import com.licenta.scheduler.exceptions.RoadNotFoundException;
import com.licenta.scheduler.model.Location;
import com.licenta.scheduler.model.Road;
import com.licenta.scheduler.repository.LocationRepository;
import com.licenta.scheduler.repository.RoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.lang.Long.parseLong;

@CrossOrigin(origins = "*")
@RestController
public class RoadController {

    @Autowired
    RoadRepository roadRepository;

    @Autowired
    LocationRepository locationRepository;

    @GetMapping("/roads")
    public List<Road> getAllRoads() {
        return roadRepository.findAll();
    }

    @GetMapping("/roads/{id}")
    public Road getSpecifiedRoad(@PathVariable Long id) {
        return roadRepository.findById(id)
                .orElseThrow(() -> new RoadNotFoundException(id));
    }

    @PostMapping("/roads/{idStart}/{idFinish}")
    public String addRoad(@PathVariable("idStart") String idStart, @PathVariable("idFinish") String idFinish, @RequestBody String cost) {
        Long startIdLong = parseLong(idStart);
        Long finishingIdLong = parseLong(idFinish);
        Long costLong = parseLong(cost);
        Optional<Location> startingLocation = locationRepository.findById(startIdLong);
        Optional<Location> finishingLocation = locationRepository.findById(finishingIdLong);
        if (startingLocation.isPresent() && finishingLocation.isPresent()) {
            Road existingRoad = roadRepository.findByStartingLocationAndFinishingLocation(startingLocation.get(), finishingLocation.get());
            if( existingRoad != null) {
                existingRoad.setCost(costLong);
                roadRepository.save(existingRoad);
            }
            else
            {
                Road newRoad = new Road();
                newRoad.setStartingLocation(startingLocation.get());
                newRoad.setFinishingLocation(finishingLocation.get());
                newRoad.setCost(costLong);
                roadRepository.save(newRoad);
            }
            return "OK";
        }

        return "Fail";
    }
}
