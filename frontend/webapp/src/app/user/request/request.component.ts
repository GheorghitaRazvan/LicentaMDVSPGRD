import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { CityLocation } from 'src/app/models/location';
import { LocationsService } from 'src/app/services/locations.service';
import { TripsService } from 'src/app/services/trips.service';

@Component({
  selector: 'app-request',
  templateUrl: './request.component.html',
  styleUrls: ['./request.component.css']
})
export class RequestComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  submitted = false;
  locations: CityLocation[];

  constructor(
    private formBuilder: FormBuilder,
    private locationService: LocationsService,
    private tripService: TripsService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.locations = [];
    this.locationService.getLocations().pipe(first()).subscribe({
      next: (location: CityLocation[]) => {
        location.forEach(location => {
          var newLocation = new CityLocation();
          newLocation.id = location.id;
          newLocation.name = location.name;
          this.locations.push(newLocation);
        })
      }
    })
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      selectedStartingLocation: new FormControl('', Validators.required),
      selectedFinishingLocation: new FormControl('', Validators.required),
      startingTimeControl: new FormControl('', [
        Validators.required,
        Validators.pattern(/^(0[8-9]|1[0-9]|2[0-3]):[0-5][0-9]$/),
      ]),
      finishingTimeControl: new FormControl('', [
        Validators.required,
        Validators.pattern(/^(0[8-9]|1[0-9]|2[0-3]):[0-5][0-9]$/),
      ]),
      personsControl: new FormControl('', [
        Validators.required,
        Validators.pattern(/^[1-3]$/)
      ])
    });
  }

  onSubmit() {
    this.submitted = true;

    if (this.form.invalid) {
      return;
    }

    this.loading = true;
    this.tripService.addTrip(
      this.form.get('selectedStartingLocation')?.value['id'],
      this.form.get('selectedFinishingLocation')?.value['id'],
      JSON.parse(localStorage['user']).id,
      this.form.get('startingTimeControl')?.value,
      this.form.get('finishingTimeControl')?.value,
      this.form.get('personsControl')?.value
    ).pipe(first())
    .subscribe({
      next: () => {
        if(localStorage['addTrip'] == '\"OK\"')
        {
          this.goToTrips();
        }
      },
      error: error => {
        console.log(error);
        this.loading = false;
      }
    })
  }

  goToTrips(){
    this.router.navigate(['../trips'], {relativeTo: this.route});
  }
}