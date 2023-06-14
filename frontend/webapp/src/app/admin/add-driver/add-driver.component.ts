import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { first } from 'rxjs';
import { CityLocation } from 'src/app/models/location';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { LocationsService } from 'src/app/services/locations.service';

@Component({
  selector: 'app-add-driver',
  templateUrl: './add-driver.component.html',
  styleUrls: ['./add-driver.component.css']
})
export class AddDriverComponent implements OnInit {
  form!: FormGroup;
  loading = false;
  submitted = false;
  emailAlreadyInUse = false;
  depotNotFound = false;
  depots: CityLocation[];

  constructor(
    private formBuilder: FormBuilder,
    private locationService: LocationsService,
    private authService: AuthenticationService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.depots = [];
    this.locationService.getDepots().pipe(first()).subscribe({
      next: (depots: CityLocation[]) => {
        depots.forEach(depot => {
          var newDepot = new CityLocation();
          newDepot.id = depot.id;
          newDepot.name = depot.name;
          this.depots.push(newDepot);
        })
      }
    })
  }

  ngOnInit() {
    this.form = this.formBuilder.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      selectedDepot: new FormControl('', Validators.required)
    })
  }

  get fdata() {
    return this.form.controls;
  }

  onSubmit() {
    this.submitted = true;

    if(this.form.invalid) {
      return;
    }

    this.loading = true;

    this.authService.registerDriver(
      this.fdata['email'].value, 
      this.fdata['password'].value, 
      this.fdata['firstName'].value, 
      this.fdata['lastName'].value, 
      this.form.get('selectedDepot')?.value['id'])
      .pipe(first())
      .subscribe({
        next: () => {
          if(localStorage['registerDriver'] == '\"Email already in use\"')
            {
              this.emailAlreadyInUse = true;
              this.loading = false;
            }
          if(localStorage['registerDriver'] == '\"Depot not found\"') {
            this.depotNotFound = true;
            this.loading = false;
          }
          if(localStorage['registerDriver'] == '\"Account successfully created\"')
            {
              this.goToMain();
            }
        },
        error: error => {
          console.log(error);
          this.loading = false;
        }
      })
  }

  goToMain() {
    this.router.navigate(['..'], { relativeTo: this.route });
  }
}
