import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { CityLocation } from 'src/app/models/location';
import { Tour } from 'src/app/models/tour';
import { Trip } from 'src/app/models/trip';
import { Vehicle } from 'src/app/models/vehicle';
import { GraphService } from 'src/app/services/graph.service';
import { TripsService } from 'src/app/services/trips.service';

@Component({
  selector: 'app-trip-solver',
  templateUrl: './trip-solver.component.html',
  styleUrls: ['./trip-solver.component.css']
})
export class TripSolverComponent implements OnInit {
  allTours: Tour[];
  allTrips: number;
  waiting = false;
  satisfiedTrips: number;
  totalWaitingTime: number;
  tripSatisfactionPercentage: number;

  constructor(
    private graphService: GraphService,
    private tripService: TripsService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.allTours = [];
    this.allTrips = 0;
    this.satisfiedTrips = 0;
    this.totalWaitingTime = 0;
    this.tripSatisfactionPercentage = 0;
  }

  ngOnInit() {
    this.waiting = true;
    this.tripSatisfactionPercentage = 0;
    this.getAllWaitingTrips();
    this.makeToursRequest();
  }

  getAllWaitingTrips() {
    this.tripService.getAllTrips().pipe(first()).subscribe({
      next: (trips: Trip[]) => {
        trips.forEach(trip => {
          this.allTrips += 1;
        });
      },
      error: error => {
        console.log(error);
      }
    })
  }

  makeToursRequest() {
    this.graphService.getSolutionTours().pipe(first()).subscribe({
      next: (tours: Tour[]) => {
        tours.forEach(tour => {
          var newTour = new Tour();

          var depot = new CityLocation();
          newTour.trips = [];
          var vehicle = new Vehicle();

          if (tour.depot != undefined) {
            depot.name = tour.depot.name
            newTour.depot = depot;
          }
          if (tour.trips != undefined) {
            tour.trips.forEach(trip => {
              var newTrip = new Trip();
              newTrip.startingLocation = trip.startingLocation;
              newTrip.startingTime = trip.startingTime;
              newTrip.finishingLocation = trip.finishingLocation;
              newTrip.finishingTime = trip.finishingTime;
              newTrip.persons = trip.persons;

              newTour.trips?.push(newTrip);
              this.satisfiedTrips += 1;
            });
          }
          if (tour.vehicle != undefined) {
            vehicle.id = tour.vehicle.id;
            newTour.vehicle = vehicle;
          }

          this.allTours.push(newTour);

        });
        this.tripSatisfactionPercentage = (this.satisfiedTrips / this.allTrips) * 100;
        this.calculateWaitingTime();
        this.waiting = false;
      },
      error: error => {
        console.log(error);
        this.waiting = false;
      }
    })
  }

  rejectSolution() {
    this.allTours = [];
    this.satisfiedTrips = 0;
    this.totalWaitingTime = 0;
    this.tripSatisfactionPercentage = 0;
    this.waiting = true;
    this.makeToursRequest();
  }

  acceptSolution() {
    this.graphService.setSolution().pipe(first()).subscribe({
      next: () => {
        if (localStorage['solution'] === '\"Done\"')
          this.router.navigate(['..'], { relativeTo: this.route });
      }
    });
  }

  calculateWaitingTime() {
    this.allTours.forEach(tour => {
      var previousFinishingTime: string | undefined = undefined;
      tour.trips?.forEach(trip => {
        if (previousFinishingTime != undefined) {
          const previousFinishing = new Date(`1970-01-01T${previousFinishingTime}`);
          const currentStarting = new Date(`1970-01-01T${trip.startingTime}`);

          const waitingTimeInMinutes = (currentStarting.getTime() - previousFinishing.getTime()) / 1000 / 60;
          console.log(trip.startingLocation);
          console.log(trip.startingTime);
          console.log(trip.finishingLocation);
          console.log(trip.finishingTime);
          console.log(waitingTimeInMinutes);

          this.totalWaitingTime += waitingTimeInMinutes;
        }

        previousFinishingTime = trip.finishingTime;
      })
    })
    this.totalWaitingTime = Math.floor(this.totalWaitingTime);
  }

  getWaitingTimeColor(waitingTime: number): string {
    if (waitingTime < 60) {
      return 'green';
    }
    else if (waitingTime < 120) {
      return 'orange';
    }
    else {
      return 'red';
    }
  }
}
