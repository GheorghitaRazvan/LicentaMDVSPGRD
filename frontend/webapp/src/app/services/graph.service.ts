import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from 'src/environment';
import { Tour } from '../models/tour';
import { Road } from '../models/road';
import { CityLocation } from '../models/location';

@Injectable({
  providedIn: 'root'
})
export class GraphService {

  constructor(private http: HttpClient) { }

  getShortestPathCost(startId: string, finishId: string): Observable<number>{
    return this.http.get<number>(`${environment.backApi}/graph/shortest/${startId}/${finishId}`)
  }

  getSolutionTours():Observable<Tour[]> {
    return this.http.get<Tour[]>(`${environment.backApi}/graph/tours`)
     .pipe(map(response => {
      if(response) {
        return Object.values(response);
      }
      return [];
     }));
  }

  setSolution() {
    return this.http.get(`${environment.backApi}/graph/accept`, {responseType: "text"})
     .pipe(map(response => {
      localStorage.setItem('solution', JSON.stringify(response));
     }));
  }

  getAllRoads():Observable<Road[]> {
    return this.http.get<Road[]>(`${environment.backApi}/roads`)
     .pipe(map(response => {
      if(response) {
        return Object.values(response);
      }
      return [];
     }))
  }

  removeRoads(ids: string[]) {
    var payload = JSON.stringify(ids);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers, body: payload, responseType: "text" as const};
    
    return this.http.delete(`${environment.backApi}/graph/roads`, options)
      .pipe(map(response => {
        console.log(response);
        localStorage.setItem('deleteRoads', JSON.stringify(response));
      }));
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

  removeLocation(id: string) {
    var payload = JSON.stringify(id);
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const options = { headers: headers, body: payload, responseType: "text" as const};
    
    return this.http.delete(`${environment.backApi}/graph/location`, options)
      .pipe(map(response => {
        localStorage.setItem('deleteLocation', JSON.stringify(response));
      }));
  }
}
