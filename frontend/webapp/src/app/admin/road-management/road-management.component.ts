import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { CityLocation } from 'src/app/models/location';
import { Road } from 'src/app/models/road';
import { GraphService } from 'src/app/services/graph.service';

@Component({
  selector: 'app-road-management',
  templateUrl: './road-management.component.html',
  styleUrls: ['./road-management.component.css']
})
export class RoadManagementComponent implements OnInit {
  allRoads: Road[];
  selectedRoads: Road[];
  deleting = false;
  failed = false;

  constructor (
    private graphService: GraphService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.allRoads = [];
    this.selectedRoads = [];
  }

  ngOnInit() {
    this.graphService.getAllRoads().pipe(first()).subscribe({
      next: (roads: Road[]) => {
        roads.forEach(road => {
          var newRoad = new Road();
          var startingLocation = new CityLocation();
          var finishingLocation = new CityLocation();
          newRoad.id = road.id;
          newRoad.cost = road.cost;
          if(road.startingLocation != undefined) {
            startingLocation.name = road.startingLocation.name;
            newRoad.startingLocation = startingLocation;
          }
          if(road.finishingLocation != undefined) {
            finishingLocation.name = road.finishingLocation.name;
            newRoad.finishingLocation = finishingLocation;
          }

          this.allRoads.push(newRoad);
        })
      },
      error: error => {
        console.log(error);
      }
    })
  }

  toggleRoad(road: Road) {
    this.failed = false;
    const index = this.selectedRoads.findIndex(r => r.id === road.id);
    if(index === -1) {
      road.selected = true;
      this.selectedRoads.push(road);
    }
    else {
      road.selected = false;
      this.selectedRoads.splice(index, 1);
    }
  }

  deleteSelectedRoads() {
    this.deleting = true;
    var selectedRoadsIds: string[] = [];

    this.selectedRoads.forEach(element => {
      if(element.id !== undefined)
      {
        selectedRoadsIds.push(element.id.toString());
      }
    })

    this.graphService.removeRoads(selectedRoadsIds).pipe(first()).subscribe({
      next: () => {
        if(localStorage['deleteRoads'] === '\"Success\"')
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

  deselectRoads() {
    this.selectedRoads.forEach(element => {
      this.deselectRoad(element);
    });
    this.selectedRoads = [];
  }

  deselectRoad(road: Road) {
    const index = this.selectedRoads.findIndex(r => r.id === road.id)
    if(index !== -1) {
      road.selected = false;
    }
  }

  refreshPage() {
    this.router.navigate(['..'], { relativeTo: this.route})
  }

  addRoad() {
    this.router.navigate(['../addRoad'], {relativeTo: this.route});
  }
}
