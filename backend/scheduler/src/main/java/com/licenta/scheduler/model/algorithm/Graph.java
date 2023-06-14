package com.licenta.scheduler.model.algorithm;

import com.licenta.scheduler.model.Location;
import com.licenta.scheduler.model.Road;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private final List<Location> locations;

    private final List<Road> roads;

    private final int[][] adjacencyMatrix;

    private final double[][] costMatrix;

    public Graph(List<Location> locations, List<Road> roads) {
        this.locations = new ArrayList<>();
        this.roads = new ArrayList<>();
        this.locations.addAll(locations);
        this.roads.addAll(roads);
        this.adjacencyMatrix = new int[locations.size()][locations.size()];
        this.costMatrix = new double[locations.size()][locations.size()];
        for (Road road: roads
             ) {
            adjacencyMatrix[locations.indexOf(road.getStartingLocation())][locations.indexOf(road.getFinishingLocation())] = 1;
            costMatrix[locations.indexOf(road.getStartingLocation())][locations.indexOf(road.getFinishingLocation())] = road.getCost();
        }

    }

    public List<Location> getLocations() {
        return locations;
    }

    public List<Road> getRoads() {
        return roads;
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public double[][] getCostMatrix() {
        return costMatrix;
    }
}
