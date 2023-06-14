import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { CityLocation } from 'src/app/models/location';
import { Tour } from 'src/app/models/tour';
import { Trip } from 'src/app/models/trip';
import { Vehicle } from 'src/app/models/vehicle';
import { GraphService } from 'src/app/services/graph.service';

@Component({
  selector: 'app-trip-solver',
  templateUrl: './trip-solver.component.html',
  styleUrls: ['./trip-solver.component.css']
})
export class TripSolverComponent implements OnInit {
  allTours: Tour[];
  waiting = false;

  constructor (
    private graphService: GraphService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.allTours = [];
  }

  ngOnInit() {
    this.waiting = true;
    this.makeToursRequest();
  }

  makeToursRequest() {
    this.graphService.getSolutionTours().pipe(first()).subscribe({
      next: (tours: Tour[]) => {
        tours.forEach(tour => {
          var newTour = new Tour();
          
          var depot = new CityLocation();
          newTour.trips = [];
          var vehicle = new Vehicle();

          if(tour.depot != undefined)
          {
            depot.name = tour.depot.name
            newTour.depot = depot;
          }
          if(tour.trips != undefined) {
            tour.trips.forEach(trip => {
              var newTrip = new Trip();
              newTrip.startingLocation = trip.startingLocation;
              newTrip.startingTime = trip.startingTime;
              newTrip.finishingLocation = trip.finishingLocation;
              newTrip.finishingTime = trip.finishingTime;
              newTrip.persons = trip.persons;

              newTour.trips?.push(newTrip);
            });
          }
          if(tour.vehicle != undefined) {
            vehicle.id = tour.vehicle.id;
            newTour.vehicle = vehicle;
          }

          this.allTours.push(newTour);
          
        });
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
    this.waiting = true;
    this.makeToursRequest();
  }

  acceptSolution() {
    this.graphService.setSolution().pipe(first()).subscribe({
      next: () => {
        if(localStorage['solution'] === '\"Done\"')
         this.router.navigate(['..'], { relativeTo: this.route});
      }
    });
  }
}
