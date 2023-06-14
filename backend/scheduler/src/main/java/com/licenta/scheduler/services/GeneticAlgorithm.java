package com.licenta.scheduler.services;

import com.licenta.scheduler.model.Location;
import com.licenta.scheduler.model.Tour;
import com.licenta.scheduler.model.Trip;
import com.licenta.scheduler.model.Vehicle;
import com.licenta.scheduler.repository.LocationRepository;
import com.licenta.scheduler.repository.TripRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class GeneticAlgorithm {

    int[][] solutionGraph;

    double[][] costMatrix;

    List<Location> depotList;

    int depotsSize;

    List<Trip> tripList;

    int tripsSize;

    List<int[][]> population;

    int populationSize;

    int selectionSize;

    double mutationRate;

    int maxGenerations;

    int tournamentSize;

    int tries = 3;

    List<int[][]> bestTries = new ArrayList<>();

    Map<Integer, Double> populationScore;

    String driverStartingTime = "PT8H";

    String driverFinishingTime = "PT20H";

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    TripRepository tripRepository;

    @Autowired
    GraphSolver graphSolver;

    @PostConstruct
    public void postConstruct() {
        this.populationSize = 100;
        this.selectionSize = 20;
        this.mutationRate = 0.05;
        this.maxGenerations = 1000;
        this.tournamentSize = 10;

        this.populationScore = new HashMap<>();
        this.population = new ArrayList<>();
        this.depotList = locationRepository.findAllDepots();
        this.tripList = tripRepository.findAllWaiting();
        this.depotsSize = depotList.size();
        this.tripsSize = tripList.size();
        this.solutionGraph = new int[depotsSize + tripsSize][depotsSize + tripsSize];
        this.costMatrix = new double[depotsSize + tripsSize][depotsSize + tripsSize];

        getCostMatrix();
    }

    private void getCostMatrix() {
        // depot-depot
        for(int i = 0; i < this.depotsSize; i++) {
            for(int j = 0; j < this.depotsSize; j++) {
                if(i != j) {
                    this.costMatrix[i][j] = this.graphSolver.getShortestPathCost(this.depotList.get(i), this.depotList.get(j));

                }
            }
        }

        // depot-trip
        for(int i = 0; i < this.depotsSize; i++) {
            for (int j = this.depotsSize; j < this.depotsSize + this.tripsSize; j++) {
                this.costMatrix[i][j] = this.graphSolver.getShortestPathCost(
                        this.depotList.get(i),
                        this.tripList.get(j - this.depotsSize).getStartingLocation());
                if(Duration.parse(this.driverStartingTime).toMinutes() + costMatrix[i][j] >
                        this.tripList.get(j - this.depotsSize).getStartingTimeAsDuration().toMinutes())
                {
                    this.costMatrix[i][j] += 10000;
                }
            }
        }

        // trip-trip
        for(int i = this.depotsSize; i < this.depotsSize + this.tripsSize; i ++) {
            for (int j = this.depotsSize; j < this.depotsSize + this.tripsSize; j++) {
                if( i != j) {
                    this.costMatrix[i][j] = this.graphSolver.getShortestPathCost(
                            this.tripList.get(i - this.depotsSize).getFinishingLocation(),
                            this.tripList.get(j - this.depotsSize).getStartingLocation());
                    if (
                            this.tripList.get(i - this.depotsSize).getFinishingTimeAsDuration().toMinutes() + costMatrix[i][j] >
                                    this.tripList.get(j - this.depotsSize).getStartingTimeAsDuration().toMinutes()) {
                        this.costMatrix[i][j] += 10000;
                    }
                }
            }
        }

        // trip-depot
        for(int i = this.depotsSize; i < this.depotsSize + this.tripsSize; i++) {
            for (int j = 0; j < this.depotsSize; j++) {
                this.costMatrix[i][j] = this.graphSolver.getShortestPathCost(
                        this.tripList.get(i - this.depotsSize).getFinishingLocation(),
                        this.depotList.get(j));
                if(
                        this.tripList.get(i - this.depotsSize).getFinishingTimeAsDuration().toMinutes() + costMatrix[i][j] >
                                Duration.parse(this.driverFinishingTime).toMinutes()) {
                    this.costMatrix[i][j] += 10000;
                }
            }
        }
    }
    public int[][] runGeneticAlgorithm() {
        for(int i = 0; i < this.tries; i++) {
            postConstruct();

            initializePopulation();

            for (int generation = 0; generation < this.maxGenerations; generation++) {
                List<int[][]> selectedPopulation = selectPopulation();
                List<int[][]> offspringPopulation = generateOffspring(selectedPopulation);

                mutatePopulation(offspringPopulation);
                evaluatePopulation(offspringPopulation);

                this.population = offspringPopulation;
            }

            System.out.println(Arrays.deepToString(costMatrix));
            int[][] bestOfIteration = getBestGraphFromPopulation(this.population);
            bestTries.add(bestOfIteration);
        }
        bestTries.sort((try1, try2) -> Double.compare(evaluateFitness(try1), evaluateFitness(try2)));
        return bestTries.get(0);
    }

    private void initializePopulation() {
        int size = this.depotsSize + this.tripsSize;

        for(int i = 0; i < this.populationSize; i++) {
            int[][] graph = generateRandomGraph(size);

            this.population.add(graph);
        }
    }

    private int[][] generateRandomGraph(int size) {
        int[][] graph = new int[size][size];

        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                if((i < this.depotsSize && j > this.depotsSize) ||
                        (i > this.depotsSize)) {
                    int randomValue = (int) (Math.random() * 2);

                    graph[i][j] = randomValue;
                }
            }
        }

        return graph;
    }

    private List<int[][]> selectPopulation() {
        List<int[][]> selectedPopulation = new ArrayList<>();
        //selectedPopulation.addAll(this.population);
        //selectedPopulation.sort((pop1, pop2) -> Double.compare(evaluateFitness(pop1), evaluateFitness(pop2)));
        //List<int[][]> sortedPopulation = new ArrayList<>();
        for (int i = 0; i < this.selectionSize; i++) {
            //sortedPopulation.add(selectedPopulation.get(i));
            List<int[][]> tournament = getRandomPopulationTournament(this.population, this.tournamentSize);
            selectedPopulation.add(getBestIndividual(tournament));
        }

        return selectedPopulation;
        //return sortedPopulation;
    }

    private List<int[][]> getRandomPopulationTournament(List<int[][]> population, int tournamentSize) {
        List<int[][]> tournament = new ArrayList<>();

        for( int i = 0; i < tournamentSize; i++) {
            int randomIndex = (int) (Math.random() * this.population.size());
            tournament.add(population.get(randomIndex));
        }

        return tournament;
    }

    private int[][] getBestIndividual(List<int[][]> tournament) {
        int[][] bestIndividual = tournament.get(0);
        double bestIndividualFitness = evaluateFitness(bestIndividual);

        for (int i = 1; i < tournament.size(); i++) {
            int[][] individual = tournament.get(i);
            double individualFitness = evaluateFitness(individual);

            if(individualFitness < bestIndividualFitness) {
                bestIndividual = individual;
                bestIndividualFitness = individualFitness;
            }
        }

        return bestIndividual;
    }

    private List<int[][]> generateOffspring(List<int[][]> population) {
        List<int[][]> offspringPopulation = new ArrayList<>(population);

        while(offspringPopulation.size() < this.populationSize) {
            int[][] parent1 = selectParent(population);
            int[][] parent2 = selectParent(population);
            int[][] offspring = crossover(parent1, parent2);

            offspringPopulation.add(offspring);
        }

        return offspringPopulation;
    }

    private int[][] selectParent(List<int[][]> population) {
        double totalFitness = calculateTotalFitness(population);
        double wheelValue = Math.random() * totalFitness;

        for(int i = 0; i < population.size(); i++) {
            wheelValue -= evaluateFitness(population.get(i));

            if(wheelValue <= 0) {
                return population.get(i);
            }
        }

        return population.get(population.size() - 1);
    }

    private int[][] crossover(int[][] parent1, int[][] parent2) {
        int[][] offspring = new int[parent1.length][parent1.length];

        int crossoverPoint = (int) (Math.random() * (parent1.length - 1)) + 1;

        for(int i = 0; i < crossoverPoint; i++) {
            System.arraycopy(parent1[i], 0, offspring[i], 0, parent1[i].length);
        }

        for(int i = crossoverPoint; i < parent2.length; i++) {
            System.arraycopy(parent2[i], 0, offspring[i], 0, parent2[i].length);
        }

        return offspring;
    }

    private void mutatePopulation(List<int[][]> population) {
        for (int[][] graph : population) {
            if(shouldMutate()) {
                mutate(graph);
            }
        }
    }

    private boolean shouldMutate() {
        return Math.random() < this.mutationRate;
    }

    private void mutate(int[][] graph) {
        for(int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if(shouldMutate()) {
                    graph[i][j] = 1 - graph[i][j];
                }
            }
        }
    }

    private double calculateTotalFitness(List<int[][]> population) {
        double totalFitness = 0;

        for(int[][] graph : population) {
            totalFitness += evaluateFitness(graph);
        }

        return totalFitness;
    }

    private void evaluatePopulation(List<int[][]> population) {
        for(int i = 0; i < population.size(); i++) {
            this.populationScore.put(i, evaluateFitness(population.get(i)));
        }
    }

    private int[][] getBestGraphFromPopulation(List<int[][]> population) {
        int[][] bestGraph = population.get(0);
        double bestGraphFitness = this.populationScore.get(0);

        for (int i = 1; i < population.size(); i++) {
            if(this.populationScore.get(i) < bestGraphFitness) {
                bestGraph = this.population.get(i);
                bestGraphFitness = this.populationScore.get(i);
            }
        }

        System.out.println("Cost: " + bestGraphFitness);
        System.out.println("All tests: ");
        checkConditions(bestGraph);
        return bestGraph;
    }

    private  void checkConditions(int[][] graph) {
        boolean failedCheck = false;
        for(int i = 0; i < graph.length; i++) {
            for(int j = 0; j < graph.length; j++) {
                if(i == j && graph[i][j] == 1) {
                    System.out.println("Sunt pe diagonala 1");
                    failedCheck = true;
                }
                if(i < this.depotsSize && j < this.depotsSize && graph[i][j] == 1) {
                    System.out.println("Am legatura intre depouri");
                    failedCheck = true;
                }
            }
        }

        if(!failedCheck) {
            System.out.println("Nu am gresit una din Diagonala/Legatura Depouri");
        }

        int vehicleExitingSum = 0;
        int vehicleEnteringSum = 0;
        int usedVehicles = 0;
        int totalExistingVehicles = 0;
        int totalEnteringVehicles = 0;
        int totalExitingVehicles = 0;

        failedCheck = false;

        for(int i = 0; i < this.depotsSize; i++) {
            int vehicles = this.depotList.get(i).getVehicles().size();
            totalExistingVehicles+= vehicles;
            vehicleExitingSum = 0;
            vehicleEnteringSum = 0;

            for(int j = 0; j < this.depotsSize + this.tripsSize; j++) {
                vehicleExitingSum += graph[i][j];
                vehicleEnteringSum += graph[j][i];

                totalExitingVehicles += graph[i][j];
                totalEnteringVehicles += graph[i][j];
            }

            if(vehicleExitingSum > vehicles ||
                    vehicleEnteringSum > vehicles ||
                    vehicleExitingSum != vehicleEnteringSum)
            {
                System.out.println("Numarul de vehicule utilizate nu este ok");
                failedCheck = true;
            }
            usedVehicles += vehicleExitingSum;

        }

        if(!failedCheck) {
            System.out.println("Nu am gresit numarul de vehicule");
        }


        failedCheck = false;

        if(totalExitingVehicles != totalEnteringVehicles || totalExitingVehicles == 0 || usedVehicles > totalExistingVehicles) {
            System.out.println("Am gresit numarul total de vehicule");
            failedCheck = true;
        }

        if(!failedCheck) {
            System.out.println("Nu am gresit numarul total de vehicule");
        }

        int isProceededBy = 0;
        int proceedsBy = 0;

        failedCheck = false;

        for (int i = this.depotsSize; i < this.depotsSize + this.tripsSize; i++) {
            isProceededBy = 0;
            proceedsBy = 0;

            for (int j = 0; j < this.depotsSize + this.tripsSize; j++) {
                isProceededBy += graph[i][j];
                proceedsBy += graph[j][i];
            }

            if(isProceededBy != 1 || proceedsBy != 1) {
                System.out.println("Am gresit numarul de cate ori este satisfacuta o calatorie");
                failedCheck = true;
            }
        }

        if(!failedCheck) {
            System.out.println("Nu am gresit numarul de cate ori este satisfacuta o calatorie");
        }

        failedCheck = false;
        List<List<Integer>> tours = new ArrayList<>();
        tours = findAllCycles(graph);
        for (List<Integer> tour: tours
        ) {
            if(tour.get(0) < this.depotsSize && tour.stream().filter(element -> element < this.depotsSize).count() != 1) {
                System.out.println(tour.toString());
                System.out.println("Am gresit ca traseele nu incep si se termina in acelasi loc");
                failedCheck = true;
            }
        }

        if(!failedCheck) {
            System.out.println("Nu am gresit in legatura cu traseele");
        }
    }
    private double evaluateFitness(int[][] graph) {
        double fitnessScore = 0;
        boolean failedCheck = false;
        for(int i = 0; i < graph.length; i++) {
            for(int j = 0; j < graph.length; j++) {
                if(i == j && graph[i][j] == 1) {
                    fitnessScore += 10000;
                    failedCheck = true;
                }
                if(i < this.depotsSize && j < this.depotsSize && graph[i][j] == 1) {
                    fitnessScore += 500;
                    failedCheck = true;
                }
                if(graph[i][j] == 1) {
                    fitnessScore += costMatrix[i][j];
                }
            }
        }

//        if(!failedCheck) {
//            fitnessScore -= 100;
//        }

        if(fitnessScore == 0) {
            return Double.MAX_VALUE;
        }

        int vehicleExitingSum = 0;
        int vehicleEnteringSum = 0;
        int usedVehicles = 0;
        int totalExistingVehicles = 0;
        int totalEnteringVehicles = 0;
        int totalExitingVehicles = 0;

//        failedCheck = false;

        for(int i = 0; i < this.depotsSize; i++) {
            int vehicles = this.depotList.get(i).getVehicles().size();
            totalExistingVehicles += vehicles;
            vehicleExitingSum = 0;
            vehicleEnteringSum = 0;

            for(int j = 0; j < this.depotsSize + this.tripsSize; j++) {
                vehicleExitingSum += graph[i][j];
                vehicleEnteringSum += graph[j][i];

                totalExitingVehicles += graph[i][j];
                totalEnteringVehicles += graph[i][j];
            }

            if(vehicleExitingSum > vehicles ||
                    vehicleEnteringSum > vehicles ||
                    vehicleExitingSum != vehicleEnteringSum)
            {
                fitnessScore += 1000 * this.tripsSize;
                failedCheck = true;
            }

            usedVehicles += vehicleExitingSum;
        }

//        if(!failedCheck) {
//            fitnessScore -= 100;
//        }
//
//        failedCheck = false;

        if(totalExitingVehicles != totalEnteringVehicles || totalExitingVehicles == 0 || usedVehicles > totalExistingVehicles ) {
            fitnessScore += 500;
            failedCheck = true;
        }
        else {
            fitnessScore += totalExitingVehicles * 200;
        }
//
//        if(!failedCheck) {
//            fitnessScore -= 100;
//        }

//        failedCheck = false;
        List<List<Integer>> tours = new ArrayList<>();
        tours = findAllCycles(graph);

        for (List<Integer> tour: tours
        ) {
            if(tour.get(0) < this.depotsSize && tour.stream().filter(element -> element < this.depotsSize).count() != 1) {
                fitnessScore += 500;
                failedCheck = true;
            }
            if(tour.get(0) >= this.depotsSize && !doesTourContainDepot(tour)) {
                fitnessScore += 500;
                failedCheck = true;
            }
        }

//        if(!failedCheck) {
//            fitnessScore -= 100;
//        }

        int isProceededBy = 0;
        int proceedsBy = 0;

//        failedCheck = false;

        for (int i = this.depotsSize; i < this.depotsSize + this.tripsSize; i++) {
            isProceededBy = 0;
            proceedsBy = 0;

            for (int j = 0; j < this.depotsSize + this.tripsSize; j++) {
                isProceededBy += graph[i][j];
                proceedsBy += graph[j][i];
            }

            if(isProceededBy != 1 || proceedsBy != 1) {
                fitnessScore += 500;
                failedCheck = true;
            }
        }

//        if(!failedCheck) {
//            fitnessScore -= 10000;
//        }




//        for( int i = 0; i < this.depotsSize; i++) {
//            if(!checkIfTripsAreOk(graph, i)) {
//                fitnessScore += 1000;
//            }
//        }

        // depot-trip
//        for( int i = 0; i < this.depotsSize; i++) {
//            for( int j = this.depotsSize; j < this.depotsSize + this.tripsSize; j++) {
//                if(graph[i][j] == 1)
//                {
//                    if(Duration.parse(this.driverStartingTime).toMinutes() + costMatrix[i][j] >
//                            this.tripList.get(j - this.depotsSize).getStartingTimeAsDuration().toMinutes())
//                    {
//                        fitnessScore += 1000;
//                    }
//                }
//            }
//        }
        // trip-trip

//        for( int i = this.depotsSize; i < this.depotsSize + this.tripsSize; i++) {
//            for( int j = this.depotsSize; j < this.depotsSize + this.tripsSize; j++) {
//                if(graph[i][j] == 1) {
//                    if (
//                            this.tripList.get(i - this.depotsSize).getFinishingTimeAsDuration().toMinutes() + costMatrix[i][j] >
//                                    this.tripList.get(j - this.depotsSize).getStartingTimeAsDuration().toMinutes()) {
//                        fitnessScore += 1000;
//                    }
//                }
//            }
//        }

        // trip-depot

//        for( int i = this.depotsSize; i < this.depotsSize + this.tripsSize; i++) {
//
//            for ( int j = 0; j < this.depotsSize; j++) {
//                if(graph[i][j] == 1) {
//                    if(
//                            this.tripList.get(i - this.depotsSize).getFinishingTimeAsDuration().toMinutes() + costMatrix[i][j] >
//                                    Duration.parse(this.driverFinishingTime).toMinutes()) {
//                        fitnessScore += 1000;
//                    }
//                }
//            }
//        }

        return fitnessScore;
    }

    private List<List<Integer>> findAllCycles(int[][] graph) {
        int n = graph.length;
        List<List<Integer>> cycles = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            List<Integer> path = new ArrayList<>();
            path.add(i);
            findCycles(graph, i, i, path, cycles);
        }

        return cycles;
    }

    private void findCycles(int[][] graph, int startingNode, int currentNode,
                            List<Integer> path, List<List<Integer>> cycles) {
        int n = graph.length;

        for(int i = 0; i < n; i++) {
            if(graph[currentNode][i] == 1) {
                if(i == startingNode) {
                    List<Integer> cycle = new ArrayList<>(path);
                    cycles.add(cycle);
                }
                else
                    if(!path.contains(i)) {
                        path.add(i);
                        findCycles(graph, startingNode, i, path, cycles);
                        path.remove(path.size() - 1);
                    }
            }
        }
    }

    private boolean doesTourContainDepot(List<Integer> tour) {
        return tour.stream().anyMatch(element -> element < this.depotsSize);
    }

    public List<Tour> getToursFor(int[][] graph) {
        List<Tour> tours = new ArrayList<>();
        List<List<Integer>> toursForVertexes = new ArrayList<>();
        toursForVertexes = findAllCycles(graph);
        for (List<Integer> tour: toursForVertexes
        ) {
            if (tour.get(0) < this.depotsSize) {
                Tour newTour = new Tour();
                newTour.setDepot(this.depotList.get(tour.get(0)));
                for(Integer trip: tour) {
                    if(tour.indexOf(trip) != 0) {
                        newTour.addTrip(
                                this.tripList.get
                                        (trip - this.depotsSize));
                    }
                }
                List<Vehicle> availableVehicles = newTour.getDepot().getVehicles();
                for(Vehicle vehicle : availableVehicles) {
                    if(!vehicle.getWasUsed()) {
                        newTour.setVehicle(vehicle);
                        vehicle.setWasUsed();
                    }
                }
                tours.add(newTour);
            }
        }
        return tours;
    }
//    private boolean checkIfTripsAreOk(int[][] graph, int vertex) {
//        boolean allPathsAreCycles = true;
//        int pathsChecked = 0;
//        boolean[] visited = new boolean[this.depotsSize + this.tripsSize];
//
//        if(!isInCycle(vertex, visited, graph)) {
//            return false;
//        }
//
//        for (int i = 0; i < this.depotsSize + this.tripsSize; i++) {
//            if (graph[vertex][i] == 1) {
//                pathsChecked++;
//
//                visited = new boolean[this.depotsSize + this.tripsSize];
//
//                if(!isInCycle(i, visited, graph) || !visited[vertex]) {
//                    allPathsAreCycles = false;
//                    break;
//                }
//            }
//        }
//
//        if(pathsChecked == 0 && allPathsAreCycles) {
//            return false;
//        }
//
//        return allPathsAreCycles;
//    }
//
//    private boolean isInCycle(int vertex, boolean[] visited, int[][] graph) {
//        if(visited[vertex]) {
//            return true;
//        }
//
//        visited[vertex] = true;
//
//        for(int i = 0; i < this.depotsSize + this.tripsSize; i++) {
//            if(graph[vertex][i] == 1 && isInCycle(i, visited, graph)) {
//                return  true;
//            }
//        }
//
//        visited[vertex] = false;
//        return false;
//    }
}
