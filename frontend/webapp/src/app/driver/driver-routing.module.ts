import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DriverComponent } from './driver.component';
import { TripsComponent } from './trips/trips.component';

const routes: Routes = [
  {
    path: '', component: DriverComponent,
    children: [
      {path: 'trip', component: TripsComponent}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DriverRoutingModule { }
