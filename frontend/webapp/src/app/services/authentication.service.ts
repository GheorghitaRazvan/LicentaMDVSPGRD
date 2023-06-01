import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { User } from '../models/user';
import { environment } from 'src/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private userSubject: BehaviorSubject<User | null>;
  public user: Observable<User | null>;
  
  constructor(
    private router: Router,
    private http: HttpClient
  ) {
      this.userSubject = new BehaviorSubject(JSON.parse(localStorage.getItem('user')!));
      this.user = this.userSubject.asObservable();
   }

  public get userValue() {
    return this.userSubject.value;
  }

  login(email: string, password: string) {
    return this.http.post<User>(`${environment.backApi}/login`, {email, password})
      .pipe(map(user => {
        if(user != null )
        {
          localStorage.setItem('user', JSON.stringify(user));
          this.userSubject.next(user);
        }
        else
        {
          localStorage.setItem('user', JSON.stringify("Failure"));
          this.userSubject.next(user);
        }
      }));
  }

  register(email: string, password: string, firstName: string, lastName: string, phoneNumber: string) {
    return this.http.post(`${environment.backApi}/register/user`, {email, password, firstName, lastName, phoneNumber}, {responseType: 'text'})
      .pipe(map(response => {
        localStorage.setItem('register', JSON.stringify(response));
      }))
  }
}
