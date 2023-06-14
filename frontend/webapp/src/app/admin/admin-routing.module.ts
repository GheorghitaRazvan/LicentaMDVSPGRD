import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';
import { TripManagementComponent } from './trip-management/trip-management.component';
import { RoadManagementComponent } from './road-management/road-management.component';
import { LocationManagementComponent } from './location-management/location-management.component';
import { TripSolverComponent } from './trip-solver/trip-solver.component';
import { AddRoadComponent } from './add-road/add-road.component';
import { AddLocationComponent } from './add-location/add-location.component';
import { AddDriverComponent } from './add-driver/add-driver.component';

const routes: Routes = [
  {
    path: '', component: AdminComponent,
    children: [
      { path: 'reject', component: TripManagementComponent },
      { path: 'road', component: RoadManagementComponent },
      { path: 'addRoad', component: AddRoadComponent },
      { path: 'location', component: LocationManagementComponent },
      { path: 'addLocation', component: AddLocationComponent },
      { path: 'schedule', component: TripSolverComponent },
      { path: 'driver', component: AddDriverComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
