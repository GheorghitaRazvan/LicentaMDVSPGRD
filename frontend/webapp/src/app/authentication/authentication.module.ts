import { CommonModule } from "@angular/common";
import { ReactiveFormsModule } from '@angular/forms';
import { NgModule } from "@angular/core";
import { AuthenticationRoutingModule } from "./authentication-routing.module";
import { LayoutComponent } from "./layout.component";
import { LoginComponent } from "./login/login.component";
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ButtonModule } from 'primeng/button';
import { KeyFilterModule } from 'primeng/keyfilter';
import { UserRegisterComponent } from "./user-register/user-register.component";
import { DriverRegisterComponent } from "./driver-register/driver-register.component";

@NgModule({
    imports: [
        CommonModule,
        ReactiveFormsModule,
        AuthenticationRoutingModule,
        InputTextModule,
        PasswordModule,
        ButtonModule,
        KeyFilterModule
    ],
    declarations: [
        LayoutComponent,
        LoginComponent,
        UserRegisterComponent,
        DriverRegisterComponent
    ]
})

export class AuthenticationModule { }