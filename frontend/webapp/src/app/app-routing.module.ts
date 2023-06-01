import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const authenticationModule = () => import('./authentication/authentication.module').then(x => x.AuthenticationModule);
const routes: Routes = [
    { path: 'account', loadChildren: authenticationModule}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
