import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({ 
    templateUrl: 'admin.component.html',
    styleUrls: ['admin.component.css'] 
})
export class AdminComponent {
    constructor(
        private router: Router,
        private route: ActivatedRoute
    ) { }
    
    logout(){
        localStorage.clear();
        this.router.navigate(['../account/login'], {relativeTo: this.route});
    }

}