import { Component, OnInit } from '@angular/core';
import { first } from 'rxjs';
import { CityLocation } from 'src/app/models/location';
import { Trip } from 'src/app/models/trip';
import { User } from 'src/app/models/user';
import { TripsService } from 'src/app/services/trips.service';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.css']
})
export class TripsComponent implements OnInit{
  allTrips: Trip[];
  userId: number = -1;
  constructor (
    private tripsService: TripsService
  ) {
    this.allTrips = [];
  }

  ngOnInit() {
    this.tripsService.getDriverTrips().pipe(first()).subscribe({
      next: (trips: Trip[]) => {
        trips.forEach(trip => {
          var newTrip = new Trip();
          var location = new CityLocation();
          var user = new User();
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
          var location = new CityLocation();
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
          if(trip.user != undefined)
          {
            user.firstName = trip.user.firstName;
            user.lastName = trip.user.lastName;
            user.phoneNumber = trip.user.phoneNumber;
            newTrip.user = user;
          }
          newTrip.persons = trip.persons;
          this.allTrips.push(newTrip);
        })
      },
      error: error => {
        console.log(error);
      }
    })
  }
}
