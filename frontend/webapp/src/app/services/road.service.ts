import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map } from 'rxjs';
import { environment } from 'src/environment';

@Injectable({
  providedIn: 'root'
})
export class RoadService {

  constructor(
    private http: HttpClient
  ) { }

  addRoad(startingId: string, finishingId: string, cost: string){
    return this.http.post(`${environment.backApi}/roads/${startingId}/${finishingId}`, cost, {responseType: 'text'})
     .pipe(map(response => {
       localStorage.setItem('addRoad', JSON.stringify(response))
     }));
  }
}
