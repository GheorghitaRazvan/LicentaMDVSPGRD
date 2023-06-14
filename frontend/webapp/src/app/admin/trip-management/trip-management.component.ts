import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { CityLocation } from 'src/app/models/location';
import { Trip } from 'src/app/models/trip';
import { TripsService } from 'src/app/services/trips.service';

@Component({
  selector: 'app-trip-management',
  templateUrl: './trip-management.component.html',
  styleUrls: ['./trip-management.component.css']
})
export class TripManagementComponent implements OnInit {
  allTrips: Trip[];
  selectedTrips: Trip[];
  rejecting = false;
  constructor (
    private tripsService: TripsService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.allTrips = [];
    this.selectedTrips = [];
  }

  ngOnInit() {
    this.tripsService.getAllTrips().pipe(first()).subscribe({
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
    const index = this.selectedTrips.findIndex(t => t.id === trip.id);
    if(index === -1) {
      trip.status = "Rejected";
      this.selectedTrips.push(trip);
    }
    else {
      trip.status = "Waiting";
      this.selectedTrips.splice(index, 1);
    }
  }

  rejectSelectedTrips() {
    this.rejecting = true;
    var selectedTripsIds: string[] = [];
    
    this.selectedTrips.forEach(element => {
      if(element.id !== undefined)
      {
        selectedTripsIds.push(element.id.toString());
      }
    });

    this.tripsService.rejectTrips(selectedTripsIds).pipe(first()).subscribe({
      next: () => {
        if(localStorage['rejectTrips'] === '\"Finished\"')
        {
          this.refreshPage();
        }
      }
    })

    this.rejecting = false;
  }

  deselectTrips() {
    this.selectedTrips.forEach(element => {
      this.deselectTrip(element);
    })
    this.selectedTrips = [];
  }

  deselectTrip(trip: Trip) {
    const index = this.selectedTrips.findIndex(t => t.id === trip.id);
    if(index !== -1) {
      trip.status = "Waiting";
    }
  }

  refreshPage() {
    this.router.navigate(['..'], { relativeTo: this.route });
  }
}
