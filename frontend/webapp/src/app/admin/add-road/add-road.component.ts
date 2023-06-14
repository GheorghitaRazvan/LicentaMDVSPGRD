import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { CityLocation } from 'src/app/models/location';
import { LocationsService } from 'src/app/services/locations.service';
import { RoadService } from 'src/app/services/road.service';

@Component({
  selector: 'app-add-road',
  templateUrl: './add-road.component.html',
  styleUrls: ['./add-road.component.css']
})
export class AddRoadComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  submitted = false;
  locations: CityLocation[];

  constructor( 
    private formBuilder: FormBuilder,
    private locationService: LocationsService,
    private roadService: RoadService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.locations = [];
    this.locationService.getAllLocations().pipe(first()).subscribe({
      next: (alllocations: CityLocation[]) => {
        alllocations.forEach(location => {
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
      costControl: new FormControl('', [
        Validators.required, 
        Validators.pattern('^[0-9]*$'), 
        Validators.min(0)
      ])
    })
  }

  onSubmit() {
    this.submitted = true;

    if (this.form.invalid) {
      return;
    }

    this.loading = true;

    this.roadService.addRoad(
      this.form.get('selectedStartingLocation')?.value['id'],
      this.form.get('selectedFinishingLocation')?.value['id'],
      this.form.get('costControl')?.value
    ).pipe(first())
    .subscribe({
      next: () => {
        if(localStorage['addRoad'] == '\"OK\"') {
          this.goToRoads();
        }
      },
      error: error => {
        console.log(error);
        this.loading = false;
      }
    })
  }

  goToRoads() {
    this.router.navigate(['../road'], {relativeTo: this.route})
  }
}
