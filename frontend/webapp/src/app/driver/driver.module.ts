import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DriverRoutingModule } from './driver-routing.module';
import { DriverComponent } from './driver.component';
import { TripsComponent } from './trips/trips.component';
import { FieldsetModule } from 'primeng/fieldset';
import { ButtonModule } from 'primeng/button';


@NgModule({
  declarations: [
    DriverComponent,
    TripsComponent
  ],
  imports: [
    CommonModule,
    DriverRoutingModule,
    ButtonModule,
    FieldsetModule
  ]
})
export class DriverModule { }
