import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs/operators';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-user-register',
  templateUrl: './user-register.component.html',
  styleUrls: ['./user-register.component.css']
})
export class UserRegisterComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  submitted = false;
  emailAlreadyInUse = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthenticationService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.form = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      phone: ['', [Validators.required, Validators.minLength(10)]]
    })
  }

  get fdata() { return this.form.controls; }

  onSubmit() {
    this.submitted = true;
    
    if(this.form.invalid) {
      return;
    }

    this.loading = true;
    this.authService.register(this.fdata['email'].value, this.fdata['password'].value, this.fdata['firstName'].value, this.fdata['lastName'].value, this.fdata['phone'].value)
      .pipe(first())
      .subscribe({
        next: () => {
            if(localStorage['register'] == '\"Email already in use\"')
            {
              this.emailAlreadyInUse = true;
              this.loading = false;
            }
            if(localStorage['register'] == '\"Account successfully created\"')
            {
              this.router.navigate(['../login'], { relativeTo: this.route });
            }
        },
        error: error => {
          console.log(error);
          this.loading = false;
        }
      })
  }

  goToLogin() {
    this.router.navigate(['../login'], { relativeTo: this.route });
  }
}
