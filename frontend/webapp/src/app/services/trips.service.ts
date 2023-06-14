import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, map } from 'rxjs';
import { environment } from 'src/environment';
import { Trip } from '../models/trip';

@Injectable({
  providedIn: 'root'
})
export class TripsService {

  private userId : string;

  constructor(
    private router: Router,
    private http: HttpClient
  ) { 
    var user = localStorage.getItem('user');
    if(user == null) {
      this.userId = '-1';
    }
    else
    {
      var parsedUser = JSON.parse(user);
      this.userId = parsedUser['id'];
    }
  }

  getTrips():Observable<Trip[]> {
    return this.http.get<Trip[]>(`${environment.backApi}/trips/user/${this.userId}`)
      .pipe(map(response => {
        if(response) {
          return Object.values(response);
        }
        return [];
      }));
  }

  getDriverTrips():Observable<Trip[]> {
    return this.http.get<Trip[]>(`${environment.backApi}/trips/driver/${this.userId}`)
     .pipe(map(response => {
       if(response) {
        return Object.values(response);
       }
       return [];
     }))
  }

  getAllTrips():Observable<Trip[]> {
    return this.http.get<Trip[]>(`${environment.backApi}/trips/waiting`)
      .pipe(map(response => {
        if(response) {
          return Object.values(response);
        }
        return [];
      }));
  }

  addTrip(startingId: string, finishingId: string, userId: string, startingTime: string,  finishingTime: string, persons: string){
    return this.http.post(`${environment.backApi}/addTrip`, {startingId, finishingId, userId, startingTime, finishingTime, persons}, {responseType: 'text'})
     .pipe(map(response => {
      localStorage.setItem('addTrip', JSON.stringify(response));
     }))
  }

  rejectTrips(ids: string[]) {
    var payload = JSON.stringify(ids);
    return this.http.put(`${environment.backApi}/trips/reject`, payload, { responseType: "text", headers: { 'Content-Type': 'application/json' } })
    .pipe(map(response => {
      localStorage.setItem('rejectTrips', JSON.stringify(response));
    }))
  }
}
