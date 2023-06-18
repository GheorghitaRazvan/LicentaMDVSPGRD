import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';
import { Trip } from 'src/app/models/trip';
import { CityLocation } from 'src/app/models/location';
import { TripsService } from 'src/app/services/trips.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.css']
})
export class TripsComponent implements OnInit{
  allTrips : Trip[];
  selectedTrip: Trip;
  selected = false;
  failed = false;
  deleting = false;
  constructor (
    private router: Router,
    private route: ActivatedRoute,
    private tripsService: TripsService
  ) {
    this.allTrips = [];
    this.selectedTrip = new Trip;
  }

  ngOnInit() {
    this.tripsService.getTrips().pipe(first()).subscribe({
      next: (trips: Trip[]) => { 
        trips.forEach(trip => {
            var newTrip = new Trip();
            var location = new CityLocation();
            newTrip.id = trip.id;
            if(trip.startingLocation != undefined && 
              "id" in trip.startingLocation &&
              "name" in trip.startingLocation 
            )
            {
              location.id = trip.startingLocation.id;
              location.name = trip.startingLocation.name;
              newTrip.startingLocation = location;
            }
            location = new CityLocation();
            if(trip.finishingLocation != undefined && 
              "id" in trip.finishingLocation &&
              "name" in trip.finishingLocation
              )
            {
              location.id = trip.finishingLocation.id;
              location.name = trip.finishingLocation.name;
              newTrip.finishingLocation = location;
            }
            newTrip.startingTime = trip.startingTime;
            newTrip.finishingTime = trip.finishingTime;
            if(trip.status == null) {
              newTrip.status = "Waiting";
            }
            else
            {
              newTrip.status = trip.status;
            }
            
            newTrip.persons = trip.persons;
            this.allTrips.push(newTrip);     
        });
      },
      error: error => {
        console.log(error);
      }
    })
  }

  toggleTrip(trip: Trip) {
    if (trip.status === 'Waiting') {
      this.failed = false;
      trip.selected = !trip.selected;
  
      if (trip.selected) {
        if (this.selectedTrip !== trip) {
          this.selectedTrip.status = 'Waiting';
          this.selectedTrip.selected = false;
        }
  
        trip.status = 'Rejected';
        this.selectedTrip = trip;
        this.selected = true;
      } else {
        this.selected = false;
        this.selectedTrip = new Trip();
      }
    } else if (trip.status === 'Rejected' && trip.selected) {
      trip.status = 'Waiting';
      trip.selected = false;
      this.selected = false;
      this.selectedTrip = new Trip();
    }
  }

  cancelTrip() {
    this.deleting = true;
    var selectedTripId: string;

    if(this.selectedTrip.id !== undefined) {
      selectedTripId = this.selectedTrip.id;
      this.tripsService.rejectTrips([selectedTripId]).pipe(first()).subscribe({
        next: () => {
          if(localStorage['rejectTrips'] === '\"Finished\"')
          {
            this.refreshPage();
          }
          else
          {
            this.deleting = false;
            this.failed = true;
          }
        }
      })
    }

    this.deleting = false;
  }

  refreshPage() {
    this.router.navigate(['../request'], { relativeTo: this.route })
  }
}
