package com.licenta.scheduler.services;

import com.licenta.scheduler.model.Location;
import com.licenta.scheduler.model.Road;
import com.licenta.scheduler.model.algorithm.Graph;
import com.licenta.scheduler.repository.LocationRepository;
import com.licenta.scheduler.repository.RoadRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GraphSolver {
    Graph graph;

    int locations;

    int[][] transposeAdjacencyMatrix;

    Map<Integer, double[]> shortestPathsCost = new HashMap<Integer, double[]>();

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    RoadRepository roadRepository;

    @PostConstruct
    public void postConstruct() {
        List<Location> locationList = locationRepository.findAll();
        List<Road> roadList = roadRepository.findAll();
        this.graph = new Graph(locationList, roadList);
        this.locations = locationList.size();
        this.transposeAdjacencyMatrix = new int[locations][locations];
        getTransposeMatrix();
    }

    public void getTransposeMatrix() {

        for(int i = 0; i < this.locations; i++)
        {
            for(int j = 0; j < this.locations; j++)
            {
                this.transposeAdjacencyMatrix[i][j] = this.graph.getAdjacencyMatrix()[j][i];
            }
        }
    }

    public int[][] getTransposeMatrix(Graph graph) {

        int size = graph.getLocations().size();
        int[][] matrix = new int[size][size];

        for(int i = 0; i < size; i++)
        {
            for(int j = 0; j < size; j++)
            {
                matrix[i][j] = graph.getAdjacencyMatrix()[j][i];
            }
        }
        return matrix;
    }

    public void DFSfunction(int location, int[][] adjacencyMatrix, Boolean[] visited)
    {
        visited[location] = true;
        System.out.println(location);
        for(int i = 0; i < adjacencyMatrix.length; i++)
        {
            if(adjacencyMatrix[location]
                    [i] == 1 &&

                    !visited[i])
            {
                DFSfunction(i, adjacencyMatrix, visited);
            }
        }
    }

    public String isConnected() {
        postConstruct();

        Boolean[] visited = new Boolean[locations + 1];

        for(int i = 0; i < locations; i++)
        {
            visited[i] = false;
        }

        DFSfunction(1, this.graph.getAdjacencyMatrix(), visited);

        for(int i = 0; i < locations; i++)
        {
            if(!visited[i])
            {
                return "Graph is not connected";
            }
        }

        for(int i = 0; i < locations; i++)
        {
            visited[i] = false;
        }

        DFSfunction(1, this.transposeAdjacencyMatrix, visited);

        for(int i = 0; i < locations; i++)
        {
            if(!visited[i])
            {
                return "Graph is not connected";
            }
        }

        return "Graph is connected";
    }

    public Boolean isConnected(Graph graph) {
        int size = graph.getLocations().size();
        Boolean[] visited = new Boolean[size];

        Arrays.fill(visited, false);

        DFSfunction(0, graph.getAdjacencyMatrix(), visited);

        for (int i = 0; i < size; i++) {
            if (!visited[i]) {
                return false;
            }
        }

        Arrays.fill(visited, false);

        int[][] transposeMatrix = this.getTransposeMatrix(graph);

        DFSfunction(0, transposeMatrix, visited);

        for (int i = 0; i < size; i++) {
            if (!visited[i]) {
                return false;
            }
        }

        return true;
    }

    public int getMinimumDistanceVertexIndex(double[] distances, Boolean[] isShortestPathSet) {
        double min = Integer.MAX_VALUE;
        int minIndex = -1;

        for(int i = 0; i < locations; i++) {
            if(!isShortestPathSet[i] && distances[i] <= min) {
                min = distances[i];
                minIndex = i;
            }
        }

        return  minIndex;
    }

    public void Dijkstra(double[][] costMatrix, int startingLocationIndex) {
        double[] distances = new double[this.locations + 1];

        Boolean[] isShortestPathSet = new Boolean[this.locations + 1];

        for (int i = 0; i < this.locations; i ++) {
            distances[i] = Integer.MAX_VALUE;
            isShortestPathSet[i] = false;
        }

        distances[startingLocationIndex] = 0;

        for (int i = 0; i < this.locations; i++) {
            int closestLocation = getMinimumDistanceVertexIndex(distances, isShortestPathSet);

            isShortestPathSet[closestLocation] = true;

            for (int location = 0; location < this.locations; location++) {
                if(!isShortestPathSet[location] &&
                        costMatrix[closestLocation][location] != 0 &&
                        distances[closestLocation] != Integer.MAX_VALUE &&
                        distances[closestLocation] + costMatrix[closestLocation][location] < distances[location]) {
                    distances[location] = distances[closestLocation] + costMatrix[closestLocation][location];
                }
            }
        }

        this.shortestPathsCost.put(startingLocationIndex, distances);
    }

    public double getShortestPathCost(Location start, Location finish) {
        postConstruct();

        int startLocationIndex = this.graph.getLocations().indexOf(start);
        int finishLocationIndex = this.graph.getLocations().indexOf(finish);

        if(startLocationIndex == -1 || finishLocationIndex == -1)
        {
            System.out.println("Something went wrong");
            return -1;
        }

        if(this.shortestPathsCost == null || this.shortestPathsCost.get(startLocationIndex) == null)
        {
            Dijkstra(this.graph.getCostMatrix(), startLocationIndex);
        }

        return this.shortestPathsCost.get(startLocationIndex)[finishLocationIndex];
    }

    public String printgraph() {
        StringBuilder string = new StringBuilder();
        string.append("Locations: ");
        for (Location l : this.graph.getLocations()
             ) {
            string.append(l.getName());
        }
        string.append("Roads: ");
        for (Road r : this.graph.getRoads())
        {
            string.append(r.getStartingLocation().getName());
            string.append("->");
            string.append(r.getFinishingLocation().getName());
            string.append(" ");
            string.append(r.getCost());
        }
        string.append("\nAdjacencyMatrix:\n");
        for(int i = 0 ; i < locations; i++)
        {
            for (int j = 0; j< locations; j++)
            {
                string.append(this.graph.getAdjacencyMatrix()[i][j]);
            }
            string.append("\n");
        }
        return string.toString();
    }
}
