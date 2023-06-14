package com.licenta.scheduler.controller;

import com.licenta.scheduler.exceptions.TripNotFoundException;
import com.licenta.scheduler.model.Driver;
import com.licenta.scheduler.model.Trip;
import com.licenta.scheduler.model.frontData.FrontTrip;
import com.licenta.scheduler.repository.DriverRepository;
import com.licenta.scheduler.repository.LocationRepository;
import com.licenta.scheduler.repository.TripRepository;
import com.licenta.scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.Long.parseLong;

@CrossOrigin(origins = "*")
@RestController
public class TripController {

    @Autowired
    TripRepository tripRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DriverRepository driverRepository;

    @GetMapping("/trips")
    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    @GetMapping("/trips/waiting")
    public List<Trip> getAllWaitingTrips() {
        return  tripRepository.findAllWaiting();
    }

    @GetMapping("/trips/trip/{id}")
    public Trip getSpecifiedTrip(@PathVariable Long id) {
        return tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException(id));
    }

    @GetMapping("/trips/user/{id}")
    public List<Trip> getUserTrips(@PathVariable Long id) {
        return tripRepository.findByUserId(id);
    }

    @GetMapping("/trips/driver/{id}")
    public List<Trip> getDriverTrips(@PathVariable Long id) {
        Optional<Driver> driver = driverRepository.findById(id);
        List<Trip> driverTrips = new ArrayList<>();
        if(driver.isPresent())
        {
            Driver actualDriver = driver.get();
            driverTrips = tripRepository.findByVehicleId(actualDriver.getVehicle().getId());
        }
        return driverTrips;
    }


    /*

    @PostMapping("/roads/{idStart}/{idFinish}")
    public Road addRoad(@PathVariable("idStart") Long idStart, @PathVariable("idFinish") Long idFinish, @RequestBody double cost) {
        Optional<Location> startingLocation = locationRepository.findById(idStart);
        Optional<Location> finishingLocation = locationRepository.findById(idFinish);
        if (startingLocation.isPresent() && finishingLocation.isPresent()) {
            Road newRoad = new Road();
            newRoad.setStartingLocation(startingLocation.get());
            newRoad.setFinishingLocation(finishingLocation.get());
            newRoad.setCost(cost);
            roadRepository.save(newRoad);
            return newRoad;
        }

        return null;
    }
   */
    @PostMapping("/trips")
    public String addTrip(@RequestBody Trip trip) {
        if(locationRepository.findById(trip.getStartingLocation().getId()).isEmpty() ||
                locationRepository.findById(trip.getFinishingLocation().getId()).isEmpty()
        )
        {
            return "The starting or finishing location of the trip does not exist in the database";
        }

        if(userRepository.findById(trip.getUser().getId()).isEmpty())
        {
            return "The trip is associated with a non-existing user in the database";
        }

        tripRepository.save(trip);

        return "Trip succesfully added in the database";
    }

    @PostMapping("/addTrip")
    public String addFrontTrip(@RequestBody FrontTrip trip){
        if(locationRepository.findById(trip.getStartingId()).isEmpty() ||
                locationRepository.findById(trip.getFinishingId()).isEmpty()
        )
        {
            return "Locations";
        }

        if(userRepository.findById(trip.getUserId()).isEmpty())
        {
            return "User";
        }
        Trip newTrip = new Trip();
        newTrip.setUser(userRepository.findById(trip.getUserId()).get());
        newTrip.setStartingLocation(locationRepository.findById(trip.getStartingId()).get());
        newTrip.setStartingTime(trip.getStartingTime());
        newTrip.setFinishingLocation(locationRepository.findById(trip.getFinishingId()).get());
        newTrip.setFinishingTime(trip.getFinishingTime());
        newTrip.setStatus("Waiting");
        newTrip.setPersons(trip.getPersons());

        tripRepository.save(newTrip);

        return "OK";
    }

    @PutMapping("/trips/reject")
    public String rejectTrips(@RequestBody List<String> ids) {
        for (String id: ids
             ) {
            Long longId = parseLong(id);
            if(tripRepository.findById(longId).isPresent()){
                Trip trip = tripRepository.findById(longId).get();
                trip.setStatus("Rejected");
                tripRepository.save(trip);
            }
        }
        return "Finished";
    }
}
