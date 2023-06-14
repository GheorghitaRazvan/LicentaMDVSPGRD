package com.licenta.scheduler.controller;

import com.licenta.scheduler.model.*;
import com.licenta.scheduler.model.algorithm.Graph;
import com.licenta.scheduler.repository.DriverRepository;
import com.licenta.scheduler.repository.LocationRepository;
import com.licenta.scheduler.repository.RoadRepository;
import com.licenta.scheduler.repository.TripRepository;
import com.licenta.scheduler.services.GeneticAlgorithm;
import com.licenta.scheduler.services.GraphSolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Long.parseLong;

@CrossOrigin(origins = "*")
@RestController
public class GraphController {

    @Autowired
    GraphSolver graphSolver;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    GeneticAlgorithm geneticAlgorithm;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    RoadRepository roadRepository;

    @Autowired
    DriverRepository driverRepository;

    private int[][] graph;

    private List<Tour> tours = new ArrayList<>();

    private Boolean toursExist = false;

    @GetMapping("/graph/connected")
    public String isGraphConnected(){
        return graphSolver.isConnected();
    }

    @GetMapping("/graph/shortest/{idStart}/{idFinish}")
    public double getShortestPathCost(@PathVariable("idStart") Long idStart, @PathVariable("idFinish") Long idFinish) {
        Optional<Location> startingLocation = locationRepository.findById(idStart);
        Optional<Location> finishingLocation = locationRepository.findById(idFinish);
        if(startingLocation.isPresent() && finishingLocation.isPresent())
        {
            return graphSolver.getShortestPathCost(startingLocation.get(), finishingLocation.get());
        }

        return -1;
    }

    @GetMapping("/graph/solve")
    public int[][] getSolvedGraph(){
        return this.geneticAlgorithm.runGeneticAlgorithm();
    }

    @GetMapping("/graph/tours")
    public List<Tour> getTours() {
        this.graph = this.geneticAlgorithm.runGeneticAlgorithm();
        this.tours = this.geneticAlgorithm.getToursFor(this.graph);
        this.toursExist = true;
        return this.tours;
    }

    @GetMapping("/graph/graph")
    public int[][] getGraph() {
        return this.graph;
    }

    @GetMapping("/graph/accept")
    public String acceptSolution() {
        if(this.toursExist) {
            for(Tour tour : this.tours) {
                for(Trip trip: tour.getTrips()) {
                    Trip repoTrip = this.tripRepository.findById(trip.getId()).get();
                    repoTrip.setStatus("Accepted");
                    repoTrip.setVehicle(tour.getVehicle());
                    this.tripRepository.save(repoTrip);
                }
            }

            List<Trip> allTripsWaiting = this.tripRepository.findAllWaiting();
            for(Trip waitingTrip : allTripsWaiting) {
                waitingTrip.setStatus("Rejected");
                this.tripRepository.save(waitingTrip);
            }

            return "Done";
        }
        else {
            return "No tours to accept";
        }
    }

    @DeleteMapping("/graph/roads")
    public String removeRoads(@RequestBody List<String> roadIds) {
        List<Long> roadLongIds = new ArrayList<>();
        for (String id : roadIds
             ) {
            Long longId = parseLong(id);
            roadLongIds.add(longId);
        }

        List<Road> roadsToRemove = roadRepository.findAllById(roadLongIds);
        List<Location> locations = locationRepository.findAll();

        List<Road> roadsAfterRemoval = removeRoadsFromList(roadRepository.findAll(), roadsToRemove);
        Graph tempGraph = new Graph(locations, roadsAfterRemoval);
        boolean connected = this.graphSolver.isConnected(tempGraph);

        if(connected) {
            roadRepository.deleteAll(roadsToRemove);
            return "Success";
        }
        else {
            return "Failure";
        }
    }

    @DeleteMapping("/graph/location")
    public String removeLocation(@RequestBody String locationId) {
        Long longId = parseLong(locationId);

        if(locationRepository.findById(longId).isPresent())
        {
            Location location = locationRepository.findById(longId).get();

            List<Road> roadsToRemove = roadRepository.findAllByStartingLocationOrFinishingLocation(location, location);

            List<Road> roadsAfterRemoval = removeRoadsFromList(roadRepository.findAll(), roadsToRemove);
            List<Location> locationsAfterRemoval = removeLocationFromList(locationRepository.findAll(), location);

            Graph tempGraph = new Graph(locationsAfterRemoval, roadsAfterRemoval);
            boolean connected = this.graphSolver.isConnected(tempGraph);

            if(connected) {
                roadRepository.deleteAll(roadsToRemove);
                if(Objects.equals(location.getType(), "Depot")) {
                    for (Vehicle vehicle: location.getVehicles()
                         ) {
                        List<Trip> tripsServedByVehicle = tripRepository.findByVehicleId(vehicle.getId());
                        for (Trip trip: tripsServedByVehicle
                             ) {
                            trip.setStatus("Waiting");
                            trip.setVehicle(null);
                            tripRepository.save(trip);
                        }
                        Driver driver = driverRepository.findByVehicleId(vehicle.getId());
                        if(driver != null) {
                            driver.setVehicle(null);
                            driverRepository.save(driver);
                        }
                    }
                }
                locationRepository.delete(location);
                return "Success";
            }
            else {
                return "Failure";
            }
        }
        return "Failure";
    }

    private List<Road> removeRoadsFromList(List<Road> fullList, List<Road> roadsToRemove) {
        List<Road> updatedList = new ArrayList<>(fullList);
        updatedList.removeAll(roadsToRemove);
        return updatedList;
    }

    private List<Location> removeLocationFromList(List<Location> fullList, Location locationToRemove) {
        List<Location> updatedList = new ArrayList<>(fullList);
        updatedList.remove(locationToRemove);
        return updatedList;
    }
}
