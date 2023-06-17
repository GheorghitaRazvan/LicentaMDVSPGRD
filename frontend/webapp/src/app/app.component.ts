import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'webapp';

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.goToLogin();
  }

  goToLogin() {
    this.router.navigate(['./account/login'], { relativeTo: this.route });
  }
}
