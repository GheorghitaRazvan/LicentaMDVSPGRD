import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { UserRoutingModule } from './user-routing.module';
import { TripsComponent } from './trips/trips.component';
import { RequestComponent } from './request/request.component';
import { UserComponent } from './user.component';

import { FieldsetModule } from 'primeng/fieldset';
import { ReactiveFormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';


@NgModule({
  declarations: [
    UserComponent,
    TripsComponent,
    RequestComponent,
  ],
  imports: [
    CommonModule,
    UserRoutingModule,
    FieldsetModule,
    ReactiveFormsModule,
    ButtonModule,
    DropdownModule,
    InputTextModule
  ]
})
export class UserModule { }
