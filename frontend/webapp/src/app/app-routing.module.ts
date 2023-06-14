import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const authenticationModule = () => import('./authentication/authentication.module').then(x => x.AuthenticationModule);
const userModule = () => import('./user/user.module').then(x => x.UserModule);
const adminModule = () => import('./admin/admin.module').then(x => x.AdminModule);
const driverModule = () => import('./driver/driver.module').then(x => x.DriverModule);
const routes: Routes = [
    { path: 'account', loadChildren: authenticationModule},
    { path: 'user', loadChildren: userModule},
    { path: 'admin', loadChildren: adminModule},
    { path: 'driver', loadChildren: driverModule}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
