import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserComponent } from './user.component';
import { TripsComponent } from './trips/trips.component';
import { RequestComponent } from './request/request.component';

const routes: Routes = [
  {
    path: '', component: UserComponent,
    children: [
        { path: 'trips', component: TripsComponent },
        { path: 'request', component: RequestComponent }
    ]
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UserRoutingModule { }
