import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CityLocation } from '../models/location';
import { environment } from 'src/environment';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LocationsService {

  constructor(private http: HttpClient) { }

  getLocations():Observable<CityLocation[]> {
    return this.http.get<CityLocation[]>(`${environment.backApi}/location`)
     .pipe(map(response => {
        if(response) {
          return Object.values(response);
        }
        return [];
     }))
  }

  getDepots():Observable<CityLocation[]> {
    return this.http.get<CityLocation[]>(`${environment.backApi}/depots`)
     .pipe(map(response => {
       if(response) {
        return Object.values(response);
       }
       return [];
     }))
  }

  getAllLocations():Observable<CityLocation[]> {
    return this.http.get<CityLocation[]>(`${environment.backApi}/locations`)
     .pipe(map(response => {
      if(response) {
        return Object.values(response);
      }
      return [];
     }))
  }

  addLocation(name: string, type: string){
    return this.http.post(`${environment.backApi}/location`, {name, type}, {responseType: "text"})
     .pipe(map(response => {
       localStorage.setItem('addLocation', JSON.stringify(response));
     }));
  }
}
