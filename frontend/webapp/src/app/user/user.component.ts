import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({ 
    templateUrl: 'user.component.html',
    styleUrls: ['user.component.css']
})
export class UserComponent {
    constructor(
        private router: Router,
        private route: ActivatedRoute
    ) {
        this.goToTrips();
    }

    goToTrips() {
        this.router.navigate(['./trips'], {relativeTo: this.route});
    }
    logout() {
        localStorage.clear();
        this.router.navigate(['../account/login'], {relativeTo: this.route});
    }
}