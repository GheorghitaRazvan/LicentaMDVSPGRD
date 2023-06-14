import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { TripManagementComponent } from './trip-management/trip-management.component';
import { LocationManagementComponent } from './location-management/location-management.component';
import { RoadManagementComponent } from './road-management/road-management.component';
import { TripSolverComponent } from './trip-solver/trip-solver.component';
import { AdminComponent } from './admin.component';
import { FieldsetModule } from 'primeng/fieldset';
import { ButtonModule } from 'primeng/button';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { AddRoadComponent } from './add-road/add-road.component'
import { ReactiveFormsModule } from '@angular/forms';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';
import { AddLocationComponent } from './add-location/add-location.component';
import { AddDriverComponent } from './add-driver/add-driver.component';
import { PasswordModule } from 'primeng/password';
import { KeyFilterModule } from 'primeng/keyfilter';

@NgModule({
  declarations: [
    TripManagementComponent,
    LocationManagementComponent,
    RoadManagementComponent,
    TripSolverComponent,
    AdminComponent,
    AddRoadComponent,
    AddLocationComponent,
    AddDriverComponent,
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    FieldsetModule,
    ButtonModule,
    ProgressSpinnerModule,
    ReactiveFormsModule,
    ButtonModule,
    DropdownModule,
    InputTextModule,
    PasswordModule,
    KeyFilterModule
  ]
})
export class AdminModule { }
