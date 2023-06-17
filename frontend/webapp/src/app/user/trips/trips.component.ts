import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs/operators';
import { Trip } from 'src/app/models/trip';
import { CityLocation } from 'src/app/models/location';
import { TripsService } from 'src/app/services/trips.service';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.css']
})
export class TripsComponent implements OnInit{
  allTrips : Trip[];
  constructor (
    private tripsService: TripsService
  ) {
    this.allTrips = [];
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
}
