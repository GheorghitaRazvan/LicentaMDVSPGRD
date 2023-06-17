import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-driver',
  templateUrl: './driver.component.html',
  styleUrls: ['./driver.component.css']
})
export class DriverComponent {
  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) { 
    this.goToTrip();
  }

  goToTrip() {
    this.router.navigate(['./trip'], {relativeTo: this.route});
  }
  
  logout() {
    localStorage.clear();
    this.router.navigate(['../account/login'], { relativeTo: this.route });
  }
}
