import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { CityLocation } from 'src/app/models/location';
import { GraphService } from 'src/app/services/graph.service';

@Component({
  selector: 'app-location-management',
  templateUrl: './location-management.component.html',
  styleUrls: ['./location-management.component.css']
})
export class LocationManagementComponent implements OnInit {
  allLocations: CityLocation[];
  selectedLocation: CityLocation;
  deleting = false;
  failed = false;

  constructor (
    private graphService: GraphService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.allLocations = [];
    this.selectedLocation = new CityLocation();
  }

  ngOnInit() {
    this.graphService.getAllLocations().pipe(first()).subscribe({
      next: (locations: CityLocation[]) => {
        locations.forEach(location => {
          var newLocation = new CityLocation();
          newLocation.id = location.id;
          newLocation.name = location.name;
          newLocation.type = location.type;
          if(location.type === 'Depot'){
            newLocation.vehicles = location.vehicles;
          }

          this.allLocations.push(newLocation);
        })
      },
      error: error => {
        console.log(error);
      }
    })
  }

  toggleLocation(location: CityLocation) {
    this.failed = false;
    location.selected = !location.selected;
    if(location.selected) {
      this.selectedLocation.selected = false;
      this.selectedLocation = location;
    }
    else {
      this.selectedLocation = new CityLocation();
    }
  }

  deleteSelectedLocation() {
    this.deleting = true;
    var selectedLocationId: string;

    if(this.selectedLocation.id !== undefined) {
      selectedLocationId = this.selectedLocation.id;
      this.graphService.removeLocation(selectedLocationId).pipe(first()).subscribe({
        next: () => {
          if(localStorage['deleteLocation'] === '\"Success\"')
          {
            this.refreshPage();
          }
          else
          {
            this.deleting = false;
            this.failed = true;
          }
        }
      })
    }

    this.deleting = false;
  }

  refreshPage() {
    this.router.navigate(['..'], { relativeTo: this.route })
  }

  addLocation() {
    this.router.navigate(['../addLocation'], {relativeTo: this.route});
  }
}
