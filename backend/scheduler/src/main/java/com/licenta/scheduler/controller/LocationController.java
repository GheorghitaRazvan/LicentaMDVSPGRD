package com.licenta.scheduler.controller;

import com.licenta.scheduler.exceptions.LocationNotFoundException;
import com.licenta.scheduler.model.Location;
import com.licenta.scheduler.model.frontData.LocationForm;
import com.licenta.scheduler.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class LocationController {

    @Autowired
    LocationRepository locationRepository;

    @GetMapping("/locations")
    public List<Location> getAllLocations() {
        return this.locationRepository.findAll();
    }

    @GetMapping("/locations/{id}")
    public Location getSpecifiedLocation(@PathVariable Long id) {
        return this.locationRepository.findById(id)
                .orElseThrow(() -> new LocationNotFoundException(id));
    }

    @PostMapping("/locations")
    public Location addNewLocation(@RequestBody Location location) {
        return this.locationRepository.save(location);
    }

    @GetMapping("/depots")
    public List<Location> getAllDepots() {
        return this.locationRepository.findAllDepots();
    }

    @GetMapping("/location")
    public List<Location> getAllNormalLocations() { return this.locationRepository.findAllNormalLocations();}

    @PostMapping("/location")
    public String addNewLocation(@RequestBody LocationForm locationForm) {
        Location newLocation = new Location();
        newLocation.setName(locationForm.getName());
        newLocation.setType(locationForm.getType());
        newLocation.setVehicles(new ArrayList<>());

        Location savedLocation = this.locationRepository.save(newLocation);
        if(savedLocation.getName().equals(newLocation.getName()) && savedLocation.getType().equals(newLocation.getType())) {
            return "OK";
        }
        else
        {
            return "Fail";
        }
    }
}
