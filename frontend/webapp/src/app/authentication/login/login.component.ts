import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { first } from 'rxjs/operators';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
  })
export class LoginComponent implements OnInit {
    form!: FormGroup;
    loading = false;
    submitted = false;
    accountNotFound = false;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private authenticationService: AuthenticationService
    ) { }

    ngOnInit() {
        this.form = this.formBuilder.group({
            email: ['', Validators.required],
            password: ['', Validators.required]
        });
    }

    get fdata() { return this.form.controls; }

    onSubmit() {
        this.submitted = true;

        if(this.form.invalid) {
            return;
        }

        this.loading = true;
        this.authenticationService.login(this.fdata['email'].value, this.fdata['password'].value)
            .pipe(first())
            .subscribe({
                next: () => {
                    if(localStorage['user'] == '\"Failure\"')
                    {
                        this.accountNotFound = true;
                        this.loading = false;                   
                    }
                    else
                    {
                        var userType : string = '';

                        var json = JSON.parse(localStorage['user']);
                        
                        userType = json.userType;

                        if(userType == 'user')
                        {
                            this.router.navigate(['../../user'], { relativeTo: this.route });
                        }
                        if(userType == 'driver')
                        {
                            this.router.navigate(['../../driver'], { relativeTo: this.route});
                        }
                        if(userType == 'admin')
                        {
                            this.router.navigate(['../../admin'], { relativeTo: this.route });
                        }
                    }
                },
                error: error => {
                    console.log(error);
                    this.loading = false;
                }
            })
    }

    goToRegister() {
        this.router.navigate(['../user-reg'], { relativeTo: this.route });
    }
}