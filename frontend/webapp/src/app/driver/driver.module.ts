import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DriverRoutingModule } from './driver-routing.module';
import { DriverComponent } from './driver.component';
import { TripsComponent } from './trips/trips.component';
import { FieldsetModule } from 'primeng/fieldset';


@NgModule({
  declarations: [
    DriverComponent,
    TripsComponent
  ],
  imports: [
    CommonModule,
    DriverRoutingModule,
    FieldsetModule
  ]
})
export class DriverModule { }
